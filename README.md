# Simplified Trading App

Below are my questions and assumptions based on the app description.
I have contact the recruiter to clarify about the requirement but the answer is based on my understanding to complete
the test, so I decide to treat this 2 days home assignment as simplified as much as possible.

## System Flow

This application is a simplified trading system that aggregates prices from external exchanges but acts as its own
liquidity provider.
The system executes trades instantly using virtual wallets, allowing users to buy and sell crypto at the best available
price.
There are no live market participants, and the complexities of real-world exchanges, such as price slippage and order
matching, are abstracted away.

### 1. Price Aggregation

- The system fetches the latest prices from Binance and Huobi every 10 seconds.
- It stores the best bid and ask prices in the database.
- The **highest bid price** is used for **Sell** orders (users sell at the highest price).
- The **lowest ask price** is used for **Buy** orders (users buy at the lowest price).

### 2. User Places Buy/Sell Order

- Users can place orders to buy or sell **ETH** or **BTC** against **USDT**.
- The system retrieves the latest aggregated price from the database.
- The system checks the user’s wallet balance:
    - For buy orders, it checks if the user has enough USDT.
    - For sell orders, it checks if the user has enough crypto (BTC/ETH).
- If valid, the trade is executed at the current best price:
    - For buying ETH/BTC: The corresponding amount of USDT is deducted from the user's balance, and the equivalent
      amount of crypto is added.
    - For selling BTC/ETH: The corresponding amount of crypto is deducted, and the equivalent amount of USDT is
      credited.

#### Example:

- If buying 1 BTC:
    - The system checks the latest aggregated price from Binance and Huobi.
    - If the lowest "ask" price is $30,000, this price will be used for the trade.
    - The system checks if the user has enough USDT.
    - If the user has more than $30,000 USDT, the system proceeds.
    - $30,000 USDT is deducted from the user’s balance, and 1 BTC is credited.

### 3. Trade Completion

- The trade is logged, and the user's wallet is updated.
- Users can view their wallet balance and transaction history via an API.

---

## Questions and assumptions

### 1. How does the system guarantee that the user can immediately buy or sell crypto, especially if there's no matching seller at the desired price?

- **Assumption:** The system aggregates prices from Binance and Huobi, stores them internally, and acts as its own
  liquidity provider.
- The system provides crypto from its own reserves in the application’s in-memory database (H2), meaning it doesn’t need
  to wait for an external seller. The trade happens instantly as the system itself handles both sides of the
  transaction.

### 2. How can we guarantee that the trade will happen directly and immediately?

- The system is not integrated with real-time market depth or live order books. Instead, it manages balances and trades
  in its in-memory database.
- The aggregated price from Binance and Huobi is used as a reference. When a user places a trade, the system instantly
  executes it based on the latest prices.
- The system acts as an automated market maker, filling user orders instantly at the stored best price.

### 3. Where does the liquidity come from if there are no real buyers or sellers?

- The system acts as a simulated exchange with its own virtual wallet. The BTC/ETH comes from the system’s reserves,
  tracked in H2.
- The system adjusts its own balance based on the trades without relying on real liquidity providers.

### 4. What happens if a user places a trade between price updates?

- The scheduler updates the aggregated prices every 10 seconds.
- If a user places a trade between updates, they trade at the most recent price fetched from the scheduler, not a live
  price from Binance or Huobi.
- Small differences between the system's internal prices and real market prices are acceptable for this app.

### 5. What about price slippage or partial orders for large trades?

- The system does not simulate real slippage or partial orders since there are no live market participants.
- Users always get the latest aggregated price, regardless of quantity. The system assumes infinite liquidity at the
  current price, abstracting away real-world exchange complexities.

### 6. Are the users placing market orders or limit orders?

- Users are placing **market orders**, meaning the trade is executed immediately at the best available price.
- **Limit orders**, where users wait for a specific price, are not part of this functional scope.

### 7. How is immediate execution guaranteed?

- The user’s trade is executed immediately against the last aggregated price fetched from Binance or Huobi.
- The trade is guaranteed because the system assumes infinite liquidity and can always fill the order, either by selling
  crypto from its own supply or buying from the user.

---
