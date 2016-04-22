package com.gp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	

	private static final String[] SI_UNITS = { "B", "KB", "MB", "GB", "TB", "PB", "EB" };
	private static final String[] BINARY_UNITS = { "B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB" };

	public static String humanReadableByteCount(final long bytes, final boolean useSIUnits)
	{
	    final String[] units = useSIUnits ? SI_UNITS : BINARY_UNITS;
	    final int base = useSIUnits ? 1000 : 1024;

	    // When using the smallest unit no decimal point is needed, because it's the exact number.
	    if (bytes < base) {
	        return bytes + " " + units[0];
	    }

	    final int exponent = (int) (Math.log(bytes) / Math.log(base));
	    final String unit = units[exponent];
	    return String.format("%.1f %s", bytes / Math.pow(base, exponent), unit);
	}
	
	public static String humanReadableByteCount(final long bytes)
	{

		return humanReadableByteCount(bytes, false);
	}
}
