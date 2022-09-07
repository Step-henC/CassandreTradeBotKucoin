package com.example.tradingbot;

import tech.cassandre.trading.bot.dto.market.TickerDTO;
import tech.cassandre.trading.bot.dto.position.PositionDTO;
import tech.cassandre.trading.bot.dto.position.PositionRulesDTO;
import tech.cassandre.trading.bot.dto.user.AccountDTO;
import tech.cassandre.trading.bot.dto.util.CurrencyPairDTO;
import tech.cassandre.trading.bot.strategy.BasicCassandreStrategy;
import tech.cassandre.trading.bot.strategy.CassandreStrategy;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;


@CassandreStrategy
public class MyFirstStrategy extends BasicCassandreStrategy {

    @Override
    public Set<CurrencyPairDTO> getRequestedCurrencyPairs() {
        return Set.of(new CurrencyPairDTO(BTC, USDT));
    }

    @Override
    public Optional<AccountDTO> getTradeAccount(Set<AccountDTO> accounts) {
        return accounts.stream()
                .filter(a -> "trade".equals(a.getName()))
                .findFirst();
    }
    PositionRulesDTO rules = PositionRulesDTO.builder()
            .stopGainPercentage(4f)
            .stopLossPercentage(25f)
            .create();

    createLongPosition(new CurrencyPairDTO(BTC, USDT), new BigDecimal("0.01"), rules);

    @Override
    public void onTickerUpdate(TickerDTO ticker) {
        if (new BigDecimal("56000").compareTo(ticker.getLast()) == -1) {

            if (canBuy(new CurrencyPairDTO(BTC, USDT), new BigDecimal("0.01"))) {
                PositionRulesDTO rules = PositionRulesDTO.builder()
                        .stopGainPercentage(4f)
                        .stopLossPercentage(25f)
                        .build();
                createLongPosition(new CurrencyPairDTO(BTC, USDT), new BigDecimal("0.01"), rules);
            }
        }
    }
    @Override
    public void onPositionStatusUpdate(PositionDTO position) {
        if (position.getStatus() == OPENED) {
            logger.info("> New position opened : {}", position.getPositionId());
        }
        if (position.getStatus() == CLOSED) {
            logger.info("> Position closed : {}", position.getDescription());
        }
    }
}
