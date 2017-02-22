package io.raycom.system.framework.collection;

import java.util.ArrayList;

public class CUnicodeUtils {
	public static int countUnicode(String str) {
		char[] charArray = str.toCharArray();
		int unicodeNumber = 0;
		for (int inx = 0; inx < charArray.length; inx++) {
			if (isUnicode(charArray[inx]))
				unicodeNumber++;
		}
		return unicodeNumber;
	}

	/**
	 * 파라미터로 넘어온 character가 Unicode인지를 판단한다. 만일 Unicode라면 true를 반환하고, 그렇지 않다면
	 * flase를 반환한다.
	 * 
	 * @param ch - char
	 * @return boolean
	 */
	public static boolean isUnicode(char ch) {
		if (ch >= '\uAC00' && ch <= '\uD7A3')
			return true;
		else
			return false;
	}

	/**
	 * 이 Utility는 String의 특정위치에서 String을 분할 할 경우, Unicode가 잘리는지 여부를 판단하고 그 결과를
	 * 반환한다. 예로, 파라미터로 넘어온 String을 함께 넘어온 cutLength의 길이 만큼씩 잘라서 한글이 잘린다면 그 잘리는
	 * 부분을 잘리지 않게 다음 Index로 넘겨주고 다시 cutLength잘라서 반복적으로 같은 작업을 처리한다. 그 결과로는
	 * Unicode가 잘린 경우 잘린 번째 Line을 차례로 int[]에 저장해서 반환한다.
	 * 
	 * @param str - String
	 * @param cutLength - int
	 * @return int[]
	 */
	public static int[] getUnicodeLineArray(String str, int cutLength) {
		int[] resultIntValue;
		char[] charArray = str.toCharArray();
		int valueLength = str.getBytes().length;
		ArrayList resultList = new ArrayList();
		int index = 0;
		int loopIndex = 1;
		if (valueLength > cutLength) {
			for (int inx = 0; inx < charArray.length; inx++) {
				if (isUnicode(charArray[inx]))
					index = index + 2;
				else
					index++;
				if (index >= cutLength) {
					if (index > cutLength) {
						resultList.add(new Integer(loopIndex));
						inx = inx - 1;
					}
					index = 0;
					loopIndex++;
				}// end if
			}// end for
		}// end if
		resultIntValue = new int[resultList.size()];
		for (int jnx = 0; jnx < resultList.size(); jnx++) {
			resultIntValue[jnx] = ((Integer) resultList.get(jnx)).intValue();
		}
		return resultIntValue;
	}
}
