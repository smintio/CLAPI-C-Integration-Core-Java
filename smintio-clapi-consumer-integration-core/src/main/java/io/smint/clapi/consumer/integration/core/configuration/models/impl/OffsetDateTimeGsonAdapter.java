package io.smint.clapi.consumer.integration.core.configuration.models.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import io.smint.clapi.consumer.generated.JSON.OffsetDateTimeTypeAdapter;


/**
 * A custom {@link OffsetDateTime} type adapter for {@link Gson} with failover to {@code Gson} standard behavior.
 *
 * <p>
 * This type adapter tries to serialized or deserialize instanced of {@link OffsetDateTime} into a {@link String} value.
 * However it contains a failover when deserialization, if the expected data is not of type {@link String} but has been
 * serialized with standard {@code Gson} behavior for unknown classes into an object. In Gson unknown objects without a
 * type adapter are serialized to JSON objects, containing all properties.
 * </p>
 *
 * <p>
 * So basically this type adapter tries to achieve a new serialization format as {@link String} but maintains to
 * deserialize the old object based format for best backwards compatibility.
 * </p>
 */
public class OffsetDateTimeGsonAdapter extends OffsetDateTimeTypeAdapter {

    private transient boolean _hasFormatterBeenSet = false;

    public OffsetDateTimeGsonAdapter() {
        super();
        this._hasFormatterBeenSet = true;
    }

    @Override
    public void setFormat(final DateTimeFormatter dateFormat) {
        super.setFormat(dateFormat);
        this._hasFormatterBeenSet = true;
    }

    @Override
    public void write(final JsonWriter out, final OffsetDateTime date) throws IOException {
        if (!this._hasFormatterBeenSet) {
            this.setFormat(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
        super.write(out, date);
    }

    @Override
    public OffsetDateTime read(final JsonReader in) throws IOException {
        if (!this._hasFormatterBeenSet) {
            this.setFormat(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        final JsonToken token = in.peek();
        if (token == JsonToken.NUMBER) {
            final int expiresIn = in.nextInt();
            return OffsetDateTime.now(ZoneId.systemDefault()).plusSeconds(expiresIn);

        } else if (token == JsonToken.BEGIN_OBJECT) {
            // original OffsetDateTime type adapter serialize to String.
            // unfortunately it is not active by default. Thus there are some serializations that
            // have invalid object type serializations. So, read these instead.
            OffsetDateTimeDeserializationModel offsetDateTimeDeserializationModel = new Gson().getAdapter(OffsetDateTimeDeserializationModel.class).read(in);            
            
            return offsetDateTimeDeserializationModel.getOffsetDateTime();
        } else { // if STRING
            return super.read(in);
        }
    }
}
