package org.csspec.auth.config;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class UUIDGenerator {
    public static UUID getRandomUUID() {
        return UUID.randomUUID();
    }

    public static byte[] toByteArray(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static String toBase64(UUID uuid) {
        Base64.Encoder base64 = Base64.getEncoder();
        return base64.encodeToString(toByteArray(uuid));
    }
}
