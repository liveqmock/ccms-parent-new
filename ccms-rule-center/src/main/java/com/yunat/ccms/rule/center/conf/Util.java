package com.yunat.ccms.rule.center.conf;

import java.util.Collection;
import java.util.Comparator;

public class Util {

	public static Comparator<HasPosition> POSITION_COMPARATOR = new Comparator<HasPosition>() {

		@Override
		public int compare(final HasPosition o1, final HasPosition o2) {
			return o1.getPosition() - o2.getPosition();
		}
	};

	public static int maxPosition(final Collection<? extends HasPosition> hasPositions) {
		int maxPosition = 0;
		for (final HasPosition h : hasPositions) {
			final int p = h.getPosition();
			if (p > maxPosition) {
				maxPosition = p;
			}
		}
		return maxPosition;
	}

	public static int nextPosition(final Collection<? extends HasPosition> hasPositions) {
		return maxPosition(hasPositions) + 1;
	}
}
