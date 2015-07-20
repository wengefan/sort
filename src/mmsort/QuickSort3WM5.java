/*
 * QuickSort 3 Way & Median of 5
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class QuickSort3WM5 implements ISortAlgorithm {
	protected static final int PIVOTS_SIZE = 31;							// pivot list size / ピボットリストのサイズ。大きすぎなければ何でもよいが、2のベぎ乗 - 1が無駄がなくてよい。
	protected static final int SWITCH_SIZE = 50;							// size of switching to other algorithms / 他のアルゴリズムに切り替えるサイズ

	public static final <T> void sort5(final T[] array, int p1, int p2, int p3, int p4, int p5, final Comparator<? super T> comparator)
	{
		T v1 = array[p1];
		T v2 = array[p2];
		T v3 = array[p3];
		T v4 = array[p4];
		T v5 = array[p5];

		//	まず、先頭３つのソート
		if (comparator.compare(v1, v2) <= 0) {
			// v1 <= v2
			if (comparator.compare(v2, v3) <= 0) {
				// v1 <= v2 <= v3
				//array[p1] = v1;
				//array[p2] = v2;
				//array[p3] = v3;
			} else if (comparator.compare(v1, v3) <= 0) {
				// v1 <= v3 < v2
				//array[p1] = v1;
				array[p2] = v3;
				array[p3] = v2;
			} else {
				// v3 < v1 <= v2
				array[p1] = v3;
				array[p2] = v1;
				array[p3] = v2;
			}
		} else {
			// v2 < v1
			if (comparator.compare(v1, v3) <= 0) {
				// v2 < v1 <= v3
				array[p1] = v2;
				array[p2] = v1;
				//array[p3] = v3;
			} else if (comparator.compare(v2, v3) <= 0) {
				// v2 <= v3 < v1
				array[p1] = v2;
				array[p2] = v3;
				array[p3] = v1;
			} else {
				// v3 < v2 < v1
				array[p1] = v3;
				//array[p2] = v2;
				array[p3] = v1;
			}
		}

		// v4 ( = array[p4]) を挿入ソートっぽく指定位置に挿入
		if (comparator.compare(array[p2], v4) <= 0) {
			if (comparator.compare(array[p3], v4) <= 0) {
				// array[p3] <= v4
			} else {
				// array[p2] <= v4 < array[p3];
				array[p4] = array[p3];
				array[p3] = v4;
			}
		} else {
			if (comparator.compare(array[p1], v4) <= 0) {
				// array[p1] <= v4 < array[p2];
				array[p4] = array[p3];
				array[p3] = array[p2];
				array[p2] = v4;
			} else {
				// v4 < array[p1] <= array[p2];
				array[p4] = array[p3];
				array[p3] = array[p2];
				array[p2] = array[p1];
				array[p1] = v4;
			}
		}

		// v5 ( = array[p5]) を挿入ソートっぽく指定位置に挿入
		if (comparator.compare(array[p3], v5) <= 0) {
			// array[p3] <= v5
			if (comparator.compare(array[p4], v5) <= 0) {
				// array[p3] <= array[4] <= v5
			} else {
				// array[p3] <= v5 < array[p4]
				array[p5] = array[p4];
				array[p4] = v5;
			}
		} else {
			// v5 < array[p3]
			if (comparator.compare(array[p2], v5) <= 0) {
				// array[p2] <= v5 < array[p3]
				array[p5] = array[p4];
				array[p4] = array[p3];
				array[p3] = v5;
			} else {
				// v5 < array[p2] <= array[p3]
				if (comparator.compare(array[p1], v5) <= 0) {
					// array[p1] <= v5 < array[p2] <= array[p3]
					array[p5] = array[p4];
					array[p4] = array[p3];
					array[p3] = array[p2];
					array[p2] = v5;
				} else {
					// v5 < array[p1] <= array[p2] <= array[p3]
					array[p5] = array[p4];
					array[p4] = array[p3];
					array[p3] = array[p2];
					array[p2] = array[p1];
					array[p1] = v5;
				}
			}
		}
/*
		if (comparator.compare(array[p1], array[p2]) > 0)
			throw new RuntimeException("aaa1");
		if (comparator.compare(array[p2], array[p3]) > 0)
			throw new RuntimeException("aaa2");
		if (comparator.compare(array[p3], array[p4]) > 0)
			throw new RuntimeException("aaa3");
		if (comparator.compare(array[p4], array[p5]) > 0)
			throw new RuntimeException("aaa4");
*/
	}


	/**
	 * Many pivot sort (3 Way partition)
	 *
	 * メニー・ピボット・ソート (3 Way partition)
	 *
	 * internal method (Added argument the pivot array)
	 * 内部的に呼び出される。ピボットの配列（ピボット候補）を引数にもつ
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void sort3WM5(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ

		if (range < 40) {
			//InsertionSort.insertionSort(array, from, to, comparator);
			BinInsertionSort.binInsertionSort(array, from, to, comparator);
			return;
		}

		int p1 = from;
		int p5 = to - 1;
		int p3 = p1 + (p5 - p1) / 2;
		int p2 = p1 + (p3 - p1) / 2;
		int p4 = p3 + (p5 - p3) / 2;

		sort5(array, p3, p2, p1, p4, p5, comparator);	//	p1とp3を入れ替えている。これにより、p1位置に5つの中央値が来る
//		System.out.println(array[p1].toString() + "," + array[p2].toString() + "," + array[p3].toString() + "," + array[p4].toString() + "," + array[p5].toString());


		final T pivot = array[p1];	//	p1 == from となる。

		int curFrom = from + 1;		//	min index / 現在処理中位置の小さい方の位置(「 + 1」しているのは、array[from] の位置にピボット値がいるため、処理する必要がないから)
		int curTo = to - 1;			//	max index / 現在処理中位置の大きい方の位置

		// A little faster
		// 少し高速化
		//
		// +----------------+-----------------------------------------+----------------+
		// | value == pivot |                    ?                    | value == pivot |
		// +----------------+-----------------------------------------+----------------+
		// ^                ^                                         ^                 ^
		// |                |                                         |                 |
		// from          curFrom                                   curTo                to

		while (curFrom < curTo && comparator.compare(array[curFrom], pivot) == 0)
			curFrom++;
		while (curFrom < curTo && comparator.compare(pivot, array[curTo]) == 0)
			curTo--;

		if (curFrom >= curTo)
			return;

		int eqFrom = curFrom;
		int eqTo = curTo;
		while (true) {
			// Separation
			// 分割
			//
			// +----------------+---------------+---------+---------------+----------------+
			// | value == pivot | value < pivot |    ?    | value > pivot | value == pivot |
			// +----------------+---------------+---------+---------------+----------------+
			// ^                ^               ^         ^               ^                 ^
			// |                |               |         |               |                 |
			// from           eqFrom         curFrom   curTo            eqTo                to

			int comp1;		// array[curFrom] と pivot の比較値
			while ((comp1 = comparator.compare(array[curFrom], pivot)) < 0)
				curFrom++;
			int comp2;		// array[curTo] と pivot の比較値
			while ((comp2 = comparator.compare(pivot, array[curTo])) < 0)
				curTo--;
			if (curFrom > curTo)
				break;

			T work = array[curFrom];
			array[curFrom] = array[curTo];
			array[curTo] = work;

			// Elements that were in the array[curFrom] at the time of comparison is present in the array[curTo] by swap.
			// array [curTo] is replaced with the array [eqTo], to collect the value equal to the pivot value in the rear.
			// 比較時に array[curFrom] にあった要素はスワップによって array[curTo] に存在している
			// array[curTo] と array[eqTo] を入れ替えて、ピボット値と等しい値を後方にいったん集める
			if (comp1 == 0) {
				work = array[curTo];
				array[curTo] = array[eqTo];
				array[eqTo] = work;
				eqTo--;
			}
			// Elements that were in the array[curTo] at the time of comparison is present in the array[curFrom] by swap.
			// array [curFrom] is replaced with the array [eqFrom], to collect the value equal to the pivot value in the rear.
			// 比較時に array[curTo] にあった要素はスワップによって array[curFrom] に存在している
			// array[curFrom] と array[eqFrom] を入れ替えて、ピボット値と等しい値を後方にいったん集める
			if (comp2 == 0) {
				work = array[curFrom];
				array[curFrom] = array[eqFrom];
				array[eqFrom] = work;
				eqFrom++;
			}

			curFrom++;
			curTo--;
		}

		// Bring pivot value and a value equal to the center
		// ピボット値と等しい値を中央に持ってくる
		//
		// Before
		// +----------------+-----------------+-----------------------+----------------+
		// | value == pivot | value < pivot   |         value > pivot | value == pivot |
		// +----------------+-----------------+-----------------------+----------------+
		// ^        |       ^                 ^                       ^        |        ^
		// |        |       |                 |                       |        |        |
		// from     |     eqFrom      curFrom & curTo               eqTo       |        to
		//          |                                                          |
		//          +---------------+                 +------------------------+
		//                          |                 |
		// After                    v                 v
		// +-----------------+----------------+----------------+-----------------------+
		// | value < pivot   | value == pivot | value == pivot |         value > pivot |
		// +-----------------+----------------+----------------+-----------------------+
		// ^                 ^                                ^                         ^
		// |                 |                                |                         |
		// from            eqFrom                           eqTo                        to
		if (curFrom != eqFrom) {
			while (eqFrom > from) {
				curFrom--;
				eqFrom--;
				final T work = array[curFrom];
				array[curFrom] = array[eqFrom];
				array[eqFrom] = work;
			}
		} else if (eqFrom != from) {
			curFrom = from;
		}
		if (curTo != eqTo) {
			while (eqTo + 1 < to) {
				eqTo++;
				curTo++;
				final T work = array[curTo];
				array[curTo] = array[eqTo];
				array[eqTo] = work;
			}
		} else if (eqTo + 1 != to) {
			curTo = to - 1;
		}

/*
		for (int i = from; i < curFrom; i++) {
			if (comparator.compare(pivot, array[i]) <= 0)
				throw new RuntimeException("aaa");
		}
		for (int i = curFrom; i < curTo + 1; i++) {
			if (comparator.compare(pivot, array[i]) != 0)
				throw new RuntimeException("bbb");
		}
		for (int i = curTo + 1; i < to; i++) {
			if (comparator.compare(pivot, array[i]) >= 0)
				throw new RuntimeException("ccc");
		}
*/
		if (from < curFrom - 1) {
			sort3WM5(array, from, curFrom, comparator);
		}

		if (curTo < to - 1) {
			sort3WM5(array, curTo + 1, to, comparator);
		}
	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		sort3WM5(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "Quick sort (3 Way & Median of 5)";
	}
}