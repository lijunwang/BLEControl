package util;

import java.io.Closeable;
import java.io.IOException;

public final class CloseUtil {

    private CloseUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

	public static void closeIO(final Closeable... closeables) {
		if (closeables == null)
			return;
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void closeIOQuietly(final Closeable... closeables) {
		if (closeables == null)
			return;
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException ignored) {
				}
			}
		}
	}
}