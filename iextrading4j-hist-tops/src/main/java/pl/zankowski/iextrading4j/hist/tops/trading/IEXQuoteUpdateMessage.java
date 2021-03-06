package pl.zankowski.iextrading4j.hist.tops.trading;

import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;
import pl.zankowski.iextrading4j.hist.api.message.IEXMessage;
import pl.zankowski.iextrading4j.hist.api.util.IEXByteConverter;

import java.util.Arrays;
import java.util.Objects;

import static pl.zankowski.iextrading4j.hist.api.IEXMessageType.QUOTE_UPDATE;

public class IEXQuoteUpdateMessage extends IEXMessage {

    public static final int LENGTH = 42;

    private final byte flag;
    private final long timestamp;
    private final String symbol;
    private final int bidSize;
    private final IEXPrice bidPrice;
    private final IEXPrice askPrice;
    private final int askSize;

    private IEXQuoteUpdateMessage(
            final byte flag,
            final long timestamp,
            final String symbol,
            final int bidSize,
            final IEXPrice bidPrice,
            final IEXPrice askPrice,
            final int askSize) {
        super(QUOTE_UPDATE);
        this.flag = flag;
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.bidSize = bidSize;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.askSize = askSize;
    }

    public static IEXQuoteUpdateMessage createIEXMessage(final byte[] bytes) {
        final byte flag = bytes[1];
        final long timestamp = IEXByteConverter.convertBytesToLong(Arrays.copyOfRange(bytes, 2, 10));
        final String symbol = IEXByteConverter.convertBytesToString(Arrays.copyOfRange(bytes, 10, 18));
        final int bidSize = IEXByteConverter.convertBytesToInt(Arrays.copyOfRange(bytes, 18, 22));
        final IEXPrice bidPrice = IEXByteConverter.convertBytesToIEXPrice(Arrays.copyOfRange(bytes, 22, 30));
        final IEXPrice askPrice = IEXByteConverter.convertBytesToIEXPrice(Arrays.copyOfRange(bytes, 30, 38));
        final int askSize = IEXByteConverter.convertBytesToInt(Arrays.copyOfRange(bytes, 38, 42));

        return new IEXQuoteUpdateMessage(flag, timestamp, symbol, bidSize, bidPrice, askPrice, askSize);
    }

    public boolean isHalted() {
        return (flag & 0x80) != 0;
    }

    public boolean isPrePostMarketSession() {
        return (flag & 0x40) != 0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getBidSize() {
        return bidSize;
    }

    public IEXPrice getBidPrice() {
        return bidPrice;
    }

    public IEXPrice getAskPrice() {
        return askPrice;
    }

    public int getAskSize() {
        return askSize;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final IEXQuoteUpdateMessage that = (IEXQuoteUpdateMessage) o;
        return timestamp == that.timestamp &&
                bidSize == that.bidSize &&
                askSize == that.askSize &&
                flag == that.flag &&
                Objects.equals(symbol, that.symbol) &&
                Objects.equals(bidPrice, that.bidPrice) &&
                Objects.equals(askPrice, that.askPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flag, timestamp, symbol, bidSize, bidPrice, askPrice, askSize);
    }

    @Override
    public String toString() {
        return "IEXQuoteUpdateMessage{" +
                "flag=" + flag +
                ", timestamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", bidSize=" + bidSize +
                ", bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                ", askSize=" + askSize +
                "} " + super.toString();
    }
}
