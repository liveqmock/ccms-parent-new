package com.yunat.ccms.core.support.utils.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.yunat.ccms.core.support.utils.FinallyCallback;
import com.yunat.ccms.core.support.utils.exception.ExceptionHandler;

public class IOUtil {

	public static void withLine(final File file, final LineHandler lineHandler,
			final ExceptionHandler<IOException> exceptionHandler) throws RuntimeException {
		final CloseableCreaterWithFinallyCloseCallback<CloseableScanner> c//
		= new CloseableCreaterWithFinallyCloseCallback<CloseableScanner>(//
				new CloseableCreater<CloseableScanner>() {
					@Override
					public CloseableScanner create() throws IOException {
						return new CloseableScanner(new Scanner(file));
					}
				});

		io(c, new CloseableIOCallback<CloseableScanner>() {
			@Override
			public void callback(final CloseableScanner scanner) throws IOException {
				while (scanner.hasNextLine()) {
					lineHandler.handle(scanner.nextLine());
				}
			}
		}, exceptionHandler, c);
	}

	public static void withLine(final File file, final LineHandler lineHandler,
			final ExceptionHandler<IOException> exceptionHandler, final FinallyCallback finallyCallback)
			throws RuntimeException {
		io(new CloseableCreater<CloseableScanner>() {
			@Override
			public CloseableScanner create() throws IOException {
				return new CloseableScanner(new Scanner(file));
			}
		}, new CloseableIOCallback<CloseableScanner>() {
			@Override
			public void callback(final CloseableScanner scanner) throws IOException {
				while (scanner.hasNextLine()) {
					lineHandler.handle(scanner.nextLine());
				}
			}
		}, exceptionHandler, finallyCallback);
	}

	public static <C extends Closeable> void io(final CloseableCreater<C> creater, final CloseableIOCallback<C> callback)
			throws RuntimeException {
		io(creater, callback, null);
	}

	public static <C extends Closeable> void io(final CloseableCreater<C> creater,
			final CloseableIOCallback<C> callback, final ExceptionHandler<IOException> exceptionHandler)
			throws RuntimeException {
		final CloseableCreaterWithFinallyCloseCallback<C> c = new CloseableCreaterWithFinallyCloseCallback<C>(creater);
		io(c, callback, exceptionHandler, c);
	}

	public static <C extends Closeable> void io(final CloseableCreater<C> creater,
			final CloseableIOCallback<C> callback, final ExceptionHandler<IOException> exceptionHandler,
			final FinallyCallback finallyCallback) throws RuntimeException {
		C closeable = null;
		try {
			closeable = creater.create();
			callback.callback(closeable);
		} catch (final IOException e) {
			if (exceptionHandler != null) {
				exceptionHandler.handle(e);
			}
		} finally {
			if (finallyCallback != null) {
				finallyCallback.finallyExecute();
			}
		}
	}

	public static IOException close(final Scanner closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (final Exception e) {
				e.printStackTrace();
				return new IOException(e);
			}
		}
		return null;
	}

	/**
	 * 关闭一个Closeable,如果在关闭时发生异常,将异常返回.否则返回null
	 * 
	 * @param closeable
	 * @return
	 */
	public static IOException close(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (final IOException e) {
				e.printStackTrace();
				return e;
			}
		}
		return null;
	}

	/**
	 * 同时实现了CloseableCreater和FinallyCallback,将在finally里关闭Closeable.
	 * 
	 * @author wenjian.liang
	 * 
	 * @param <C>
	 */
	private static class CloseableCreaterWithFinallyCloseCallback<C extends Closeable> implements CloseableCreater<C>,
			FinallyCallback {
		private final CloseableCreater<C> creater;
		private C closeable;

		CloseableCreaterWithFinallyCloseCallback(final CloseableCreater<C> creater) {
			super();
			this.creater = creater;
		}

		@Override
		public void finallyExecute() {
			IOUtil.close(closeable);
		}

		@Override
		public C create() throws IOException {
			closeable = creater.create();
			return closeable;
		}
	}

	public static interface LineHandler {
		void handle(String line) throws IOException;
	}

	/**
	 * {@link Scanner}在jdk1.7之前没有实现{@link Closeable}接口,故写了这个adapter类
	 * 
	 * @author wenjian.liang
	 * 
	 */
	public static class CloseableScanner implements Closeable {
		private final Scanner scanner;

		public CloseableScanner(final Scanner scanner) {
			super();
			this.scanner = scanner;
		}

		@Override
		public int hashCode() {
			return scanner.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			return scanner.equals(obj);
		}

		@Override
		public void close() {
			scanner.close();
		}

		public IOException ioException() {
			return scanner.ioException();
		}

		public Pattern delimiter() {
			return scanner.delimiter();
		}

		public Scanner useDelimiter(final Pattern pattern) {
			return scanner.useDelimiter(pattern);
		}

		public Scanner useDelimiter(final String pattern) {
			return scanner.useDelimiter(pattern);
		}

		public Locale locale() {
			return scanner.locale();
		}

		public Scanner useLocale(final Locale locale) {
			return scanner.useLocale(locale);
		}

		public int radix() {
			return scanner.radix();
		}

		public Scanner useRadix(final int radix) {
			return scanner.useRadix(radix);
		}

		public MatchResult match() {
			return scanner.match();
		}

		@Override
		public String toString() {
			return scanner.toString();
		}

		public boolean hasNext() {
			return scanner.hasNext();
		}

		public String next() {
			return scanner.next();
		}

		public void remove() {
			scanner.remove();
		}

		public boolean hasNext(final String pattern) {
			return scanner.hasNext(pattern);
		}

		public String next(final String pattern) {
			return scanner.next(pattern);
		}

		public boolean hasNext(final Pattern pattern) {
			return scanner.hasNext(pattern);
		}

		public String next(final Pattern pattern) {
			return scanner.next(pattern);
		}

		public boolean hasNextLine() {
			return scanner.hasNextLine();
		}

		public String nextLine() {
			return scanner.nextLine();
		}

		public String findInLine(final String pattern) {
			return scanner.findInLine(pattern);
		}

		public String findInLine(final Pattern pattern) {
			return scanner.findInLine(pattern);
		}

		public String findWithinHorizon(final String pattern, final int horizon) {
			return scanner.findWithinHorizon(pattern, horizon);
		}

		public String findWithinHorizon(final Pattern pattern, final int horizon) {
			return scanner.findWithinHorizon(pattern, horizon);
		}

		public Scanner skip(final Pattern pattern) {
			return scanner.skip(pattern);
		}

		public Scanner skip(final String pattern) {
			return scanner.skip(pattern);
		}

		public boolean hasNextBoolean() {
			return scanner.hasNextBoolean();
		}

		public boolean nextBoolean() {
			return scanner.nextBoolean();
		}

		public boolean hasNextByte() {
			return scanner.hasNextByte();
		}

		public boolean hasNextByte(final int radix) {
			return scanner.hasNextByte(radix);
		}

		public byte nextByte() {
			return scanner.nextByte();
		}

		public byte nextByte(final int radix) {
			return scanner.nextByte(radix);
		}

		public boolean hasNextShort() {
			return scanner.hasNextShort();
		}

		public boolean hasNextShort(final int radix) {
			return scanner.hasNextShort(radix);
		}

		public short nextShort() {
			return scanner.nextShort();
		}

		public short nextShort(final int radix) {
			return scanner.nextShort(radix);
		}

		public boolean hasNextInt() {
			return scanner.hasNextInt();
		}

		public boolean hasNextInt(final int radix) {
			return scanner.hasNextInt(radix);
		}

		public int nextInt() {
			return scanner.nextInt();
		}

		public int nextInt(final int radix) {
			return scanner.nextInt(radix);
		}

		public boolean hasNextLong() {
			return scanner.hasNextLong();
		}

		public boolean hasNextLong(final int radix) {
			return scanner.hasNextLong(radix);
		}

		public long nextLong() {
			return scanner.nextLong();
		}

		public long nextLong(final int radix) {
			return scanner.nextLong(radix);
		}

		public boolean hasNextFloat() {
			return scanner.hasNextFloat();
		}

		public float nextFloat() {
			return scanner.nextFloat();
		}

		public boolean hasNextDouble() {
			return scanner.hasNextDouble();
		}

		public double nextDouble() {
			return scanner.nextDouble();
		}

		public boolean hasNextBigInteger() {
			return scanner.hasNextBigInteger();
		}

		public boolean hasNextBigInteger(final int radix) {
			return scanner.hasNextBigInteger(radix);
		}

		public BigInteger nextBigInteger() {
			return scanner.nextBigInteger();
		}

		public BigInteger nextBigInteger(final int radix) {
			return scanner.nextBigInteger(radix);
		}

		public boolean hasNextBigDecimal() {
			return scanner.hasNextBigDecimal();
		}

		public BigDecimal nextBigDecimal() {
			return scanner.nextBigDecimal();
		}

		public Scanner reset() {
			return scanner.reset();
		}

	}
}
