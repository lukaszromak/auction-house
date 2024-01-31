package com.lukasz.auctionhouse.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.lukasz.auctionhouse.domain.ItemStatus;

import java.io.IOException;
import static java.util.Map.entry;
import java.util.Map;

public class ItemStatusSerializer extends StdSerializer<ItemStatus> {
    private static final long serialVersionUID = 1L;
    private static final Map<String, String> map = Map.ofEntries(
            entry("NOT_BOUGHT", "Not bought"),
            entry("BOUGHT_BIN", "Bought via bin"),
            entry("BOUGHT_AUCTION", "Bought via auction")
    );
    public ItemStatusSerializer() {
        this(null);
    }
    public ItemStatusSerializer(Class<ItemStatus> t) {
        super(t);
    }
    @Override
    public void serialize(ItemStatus value,
                          JsonGenerator generator, SerializerProvider arg2) throws IOException {
        generator.writeString(map.get(value.getName()));
    }
}