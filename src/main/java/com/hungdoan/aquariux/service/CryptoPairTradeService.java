package com.hungdoan.aquariux.service;

import com.hungdoan.aquariux.common.extract.CryptoPairExtractor;
import com.hungdoan.aquariux.common.id_generator.IdGenerator;
import com.hungdoan.aquariux.data_access.spec.AssetRepository;
import com.hungdoan.aquariux.data_access.spec.TradeRepository;
import com.hungdoan.aquariux.dto.api.page.Page;
import com.hungdoan.aquariux.dto.api.page.PageRequest;
import com.hungdoan.aquariux.dto.api.trade_history.TradeHistoryResponse;
import com.hungdoan.aquariux.exception.InvalidTrade;
import com.hungdoan.aquariux.exception.OptimisticLockingFailureException;
import com.hungdoan.aquariux.model.Asset;
import com.hungdoan.aquariux.model.Price;
import com.hungdoan.aquariux.model.Trade;
import com.hungdoan.aquariux.service.spec.PriceService;
import com.hungdoan.aquariux.service.spec.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CryptoPairTradeService implements TradeService {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoPairTradeService.class);

    private static final int MAX_RETRIES_OPTIMISTIC_LOCK = 5;

    private final CryptoPairExtractor cryptoPairExtractor;

    private final IdGenerator idGenerator;

    private final PriceService priceService;

    private final AssetRepository assetRepository;

    private final TradeRepository tradeRepository;

    private final Set<String> validTradeTypes;

    public CryptoPairTradeService(PriceService priceService,
                                  CryptoPairExtractor cryptoPairExtractor,
                                  IdGenerator idGenerator,
                                  AssetRepository assetRepository,
                                  TradeRepository tradeRepository) {
        this.priceService = priceService;
        this.cryptoPairExtractor = cryptoPairExtractor;
        this.idGenerator = idGenerator;
        this.assetRepository = assetRepository;
        this.tradeRepository = tradeRepository;
        this.validTradeTypes = new HashSet<>();
        validTradeTypes.add("BUY");
        validTradeTypes.add("SELL");
    }


    @Override
    public String executeTrade(String userId, String cryptoPair, String tradeType, Double tradeAmount) {
        String[] coins = cryptoPairExtractor.extractCurrencies(cryptoPair);
        if (coins.length != 2) {
            throw new InvalidTrade(String.format("Invalid crypto pair, the system does not support this pair %s",
                    cryptoPair));
        }
        String baseCoin = coins[0];
        String quoteCoin = coins[1];

        Optional<Price> optionalLatestPrice = priceService.getPrice(cryptoPair);
        if (optionalLatestPrice.isEmpty()) {
            throw new InvalidTrade(String.format("Could not get the price of the crypto pair %s", cryptoPair));
        }
        Price latestPrice = optionalLatestPrice.get();

        if (!validTradeTypes.contains(tradeType)) {
            throw new InvalidTrade(String.format("Invalid trade type, it must be %s instead of %s",
                    validTradeTypes, tradeType));
        }
        Double tradePrice = tradeType.equals("BUY") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();

        if (tradeType.equals("BUY")) {
            return buy(userId, cryptoPair, tradeType, tradeAmount, tradePrice, baseCoin, quoteCoin);
        }
        return sell(userId, cryptoPair, tradeType, tradeAmount, tradePrice, baseCoin, quoteCoin);
    }

    @Override
    public Page<TradeHistoryResponse> fetchHistory(String userId, PageRequest pageRequest) {
        List<Trade> trades = tradeRepository.getAllTrades(userId, pageRequest);
        List<TradeHistoryResponse> tradeHistoryResponse = trades.stream().map(trade -> new TradeHistoryResponse(trade.getId(),
                trade.getUserId(), trade.getCryptoPair(), trade.getTradeType(), trade.getTradeAmount(),
                trade.getTradePrice(), trade.getTradeTimestamp())).collect(Collectors.toUnmodifiableList());

        long totalElements = tradeRepository.countTrades(userId);  // Get total count
        String lastId = trades.isEmpty() ? null : trades.get(trades.size() - 1).getId();  // Get the last ID

        return new Page<TradeHistoryResponse>(tradeHistoryResponse, lastId, pageRequest.getPageSize(), totalElements);
    }

    @Transactional
    private String buy(String userId, String cryptoPair, String tradeType, Double tradeAmount, Double tradePrice, String baseCoin, String quoteCoin) {

        for (int attempt = 0; attempt < MAX_RETRIES_OPTIMISTIC_LOCK; attempt++) {

            try {

                Map<String, Asset> userAssets = assetRepository.getAssets(userId, Arrays.asList(quoteCoin, baseCoin));

                Asset quoteCoinAsset = userAssets.get(quoteCoin);
                Double totalCost = tradePrice * tradeAmount;
                if (quoteCoinAsset.getBalance() < totalCost) {
                    throw new InvalidTrade(String.format("Insufficient %s balance, the remaining %.5f < total cost %.5f", quoteCoin, quoteCoinAsset.getBalance(), totalCost));
                }
                userAssets.put(quoteCoin, new Asset(userId, quoteCoin, quoteCoinAsset.getBalance() - totalCost, quoteCoinAsset.getVersion()));

                Asset baseCoinAsset = userAssets.getOrDefault(baseCoin, new Asset(userId, baseCoin, 0.0, 0));
                userAssets.put(baseCoin, new Asset(userId, baseCoin, baseCoinAsset.getBalance() + tradeAmount, baseCoinAsset.getVersion()));

                assetRepository.updateAssets(userAssets.values());

                Trade trade = new Trade(idGenerator.getId(), userId, cryptoPair, tradeType, tradeAmount, totalCost, ZonedDateTime.now());
                tradeRepository.save(trade);
                return trade.getId();

            } catch (OptimisticLockingFailureException e) {
                LOG.warn("Optimistic locking failure for Trade Buy {}, retrying... Attempt: {}",
                        userId + " " + cryptoPair + " " + tradeAmount + " " + tradePrice,
                        attempt);
            } catch (InvalidTrade e) {
                throw e;
            } catch (Throwable throwable) {
                LOG.error(throwable.getMessage(), throwable);
                throw new InvalidTrade(
                        String.format("The transaction for the Trade Buy %s is fail ! Please try again !", userId + " " + cryptoPair + " " + tradeAmount + " " + tradePrice),
                        throwable);
            }
        }

        throw new InvalidTrade("Failed to complete the trade after multiple attempts");
    }

    @Transactional
    private String sell(String userId, String cryptoPair, String tradeType, Double tradeAmount, Double tradePrice, String baseCoin, String quoteCoin) {
        for (int attempt = 0; attempt < MAX_RETRIES_OPTIMISTIC_LOCK; attempt++) {

            try {
                Map<String, Asset> assets = assetRepository.getAssets(userId, Arrays.asList(quoteCoin, baseCoin));
                Asset baseCoinAsset = assets.get(baseCoin);
                if (baseCoinAsset.getBalance() < tradeAmount) {
                    throw new InvalidTrade(String.format("Insufficient %s balance, the remaining %.5f < trade amount %.5f", baseCoin, baseCoinAsset.getBalance(), tradeAmount));
                }
                assets.put(baseCoin, new Asset(userId, baseCoin, baseCoinAsset.getBalance() - tradeAmount, baseCoinAsset.getVersion()));

                Asset quoteCoinAsset = assets.getOrDefault(quoteCoin, new Asset(userId, quoteCoin, 0.0, 0));
                Double totalRevenue = tradePrice * tradeAmount;
                assets.put(quoteCoin, new Asset(userId, quoteCoin, quoteCoinAsset.getBalance() + totalRevenue, quoteCoinAsset.getVersion()));

                assetRepository.updateAssets(assets.values());

                Trade trade = new Trade(idGenerator.getId(), userId, cryptoPair, tradeType, tradeAmount, totalRevenue, ZonedDateTime.now());
                tradeRepository.save(trade);
                return trade.getId();

            } catch (OptimisticLockingFailureException e) {
                LOG.warn("Optimistic locking failure for Trade Sell {}, retrying... Attempt: {}",
                        userId + " " + cryptoPair + " " + tradeAmount + " " + tradePrice,
                        attempt);
            } catch (InvalidTrade e) {
                throw e;
            } catch (Throwable throwable) {
                LOG.error(throwable.getMessage(), throwable);
                throw new InvalidTrade(
                        String.format("The transaction for the Trade Sell %s is fail ! Please try again !", userId + " " + cryptoPair + " " + tradeAmount + " " + tradePrice),
                        throwable);
            }
        }

        throw new InvalidTrade("Failed to complete the trade after multiple attempts");
    }
}