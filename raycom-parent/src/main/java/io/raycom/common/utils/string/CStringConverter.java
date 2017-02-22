package io.raycom.common.utils.string;

import java.math.BigDecimal;
import java.text.DecimalFormat;	
import java.util.ArrayList;
import java.util.StringTokenizer;


public class CStringConverter {
	public CStringConverter() {
	}

	public static String convertYn(String ynStr) {
		if (ynStr == null)
			return "";
		if (ynStr.trim().toUpperCase().equals("Y"))
			return "是";
		if (ynStr.trim().toUpperCase().equals("N"))
			return "否";
		return "";
	}

	public static String shortCutString(String str, final int limit) {

		if (str == null || limit < 4)
			return str;

		int len = str.length();
		int cnt = 0, index = 0;

		while (index < len && cnt < limit) {
			if (str.charAt(index++) < 256) 
				cnt++; 
			else
				cnt += 2; 
		}

		if (index < len)
			str = str.substring(0, index);

		return str;
	}

	public static String insert(String strTarget, int loc, String strInsert) {
		String result = null;

		StringBuffer strBuf = new StringBuffer();
		int lengthSize = strTarget.length();
		if (loc >= 0) {
			if (lengthSize < loc) {
				loc = lengthSize;
			}
			strBuf.append(strTarget.substring(0, loc));
			strBuf.append(strInsert);
			strBuf.append(strTarget.substring(loc + strInsert.length()));
		} else {
			if (lengthSize < Math.abs(loc)) {
				loc = lengthSize * (-1);
			}
			strBuf.append(strTarget.substring(0, (lengthSize - 1) + loc));
			strBuf.append(strInsert);
			strBuf.append(strTarget.substring((lengthSize - 1) + loc + strInsert.length()));
		}
		result = strBuf.toString();

		return result;
	}

	public static String delete(String strTarget, String strDelete) {
		return replace(strTarget, strDelete, "");
	}

	public static String[] split(String strTarget, String strDelim, boolean bContainNull) {
		int index = 0;
		String[] resultStrArray = null;
		resultStrArray = new String[search(strTarget, strDelim) + 1];
		String strCheck = new String(strTarget);
		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strDelim);
			if (begin == -1) {
				resultStrArray[index] = strCheck;
				break;
			} else {
				int end = begin + strDelim.length();
				if (bContainNull) {
					resultStrArray[index++] = strCheck.substring(0, begin);
				}
				strCheck = strCheck.substring(end);
				if (strCheck.length() == 0 && bContainNull) {
					resultStrArray[index] = strCheck;
					break;
				}
			}
		}
		return resultStrArray;
	}

	public static int search(String strTarget, String strSearch) {
		int result = 0;
		String strCheck = new String(strTarget);
		for (int i = 0; i < strTarget.length();) {
			int loc = strCheck.indexOf(strSearch);
			if (loc == -1) {
				break;
			} else {
				result++;
				i = loc + strSearch.length();
				strCheck = strCheck.substring(i);
			}
		}
		return result;
	}

	public static String escapeDollarMarker(String str) throws Exception {
		return CStringConverter.replace(str, "$", "\\$");
	}

	public static boolean isNull(String value) {
		return value == null;
	}

	public static String token(String value, String delim, int idx) {
		if (value == null)
			return null;
		StringTokenizer st = new StringTokenizer(value, delim);
		int i = 0;
		while (st.hasMoreTokens()) {
			if ((i++) == idx)
				return st.nextToken();
			else
				st.nextToken();
		}
		return null;
	}

	public static boolean isNone(String value) {
		return (value == null || value.length() == 0);
	}

	public static String[] divide(String target, String cut_str) {
		if (target == null)
			return new String[] { "", "" };
		if (cut_str == null || cut_str.length() == 0)
			return new String[] { target, "" };
		int idx = target.indexOf(cut_str);
		if (idx < 0)
			return new String[] { target, "" };
		else
			return new String[] { target.substring(0, idx), target.substring(idx + cut_str.length()) };
	}

	public static String[] cut(String target, String cut_str) {
		if (target == null)
			return new String[] {};
		if (cut_str == null || cut_str.length() == 0)
			return new String[] { target };
		int idx = target.indexOf(cut_str);
		if (idx < 0)
			return new String[] { target };
		ArrayList arr = new ArrayList();
		final int cut_str_len = cut_str.length();
		int next_idx = 0;
		while (idx >= 0) {
			arr.add(target.substring(next_idx, idx));
			next_idx = idx + cut_str_len;
			idx = target.indexOf(cut_str, next_idx);
			if (idx < 0) {
				arr.add(target.substring(next_idx));
				break;
			}
		}
		return (String[]) arr.toArray(new String[arr.size()]);
	}

	public static String lowerFirst(String s) {
		if (s == null || s.length() < 1)
			return s;
		return (s.substring(0, 1).toLowerCase() + s.substring(1));
	}

	/*public static String encode(Object vo) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bo);
		os.writeObject(vo);
		return new BASE64Encoder().encode(bo.toByteArray());
	}

	public static Object decode(String text) throws IOException, ClassNotFoundException {
		byte o[] = new BASE64Decoder().decodeBuffer(text);
		ByteArrayInputStream bi = new ByteArrayInputStream(o);
		ObjectInputStream os = new ObjectInputStream(bi);
		Object vo = os.readObject();
		return vo;
	}*/

	public static String fillLeft(String s, byte ch, int len) {
		if (s == null)
			s = "";
		byte[] ss;
		try {
			ss = s.getBytes("ksc5601");
		} catch (Exception e) {
			return s;
		}
		if (len <= ss.length)
			return s;
		byte[] chs = new byte[len];
		int j = len - 1;
		for (int i = ss.length - 1; i >= 0; i--)
			chs[j--] = ss[i];
		for (; j >= 0; j--)
			chs[j] = ch;
		try {
			return new String(chs, "ksc5601");
		} catch (Exception e) {
			return s;
		}
	}

	public static String fillRight(String s, byte ch, int len) {
		if (s == null)
			s = "";
		byte[] ss;
		try {
			ss = s.getBytes("ksc5601");
		} catch (Exception e) {
			return s;
		}
		if (len <= ss.length)
			return s;
		byte[] chs = new byte[len];
		int j = 0;
		for (int i = 0; i < ss.length; i++)
			chs[j++] = ss[i];
		for (; j < len; j++)
			chs[j] = ch;
		try {
			return new String(chs, "ksc5601");
		} catch (Exception e) {
			return s;
		}
	}

	public static String strip(String s, String delims) {
		if (s == null || s.length() == 0 || delims == null)
			return s;
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(s, delims);
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		return sb.toString();
	}

	public static String upperFirst(String s) {
		if (s == null || s.length() < 1)
			return s;
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}

	public static String[] stringToArray(String text, String delimiter) {
		ArrayList array = new ArrayList();
		String cur = text;
		while (cur != null && delimiter != null) {
			int i = cur.indexOf(delimiter);
			if (i < 0) {
				array.add(cur);
				cur = null;
			} else {
				array.add(cur.substring(0, i));
				cur = cur.substring(i + delimiter.length());
			}
		}
		return (String[]) array.toArray(new String[array.size()]);
	}

	public static String arrayToString(String[] values, String delimiter) {
		StringBuffer sb = new StringBuffer();
		if (values == null || values.length < 1)
			return "";
		sb.append(values[0]);
		for (int i = 1; i < values.length; i++) {
			sb.append(delimiter).append(values[i]);
		}
		return sb.toString();
	}

	public static String java2mysql(String s) {
		if (s == null)
			return null;
		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '\n')
				buf.append("\\n");
			else if (c[i] == '\t')
				buf.append("\\t");
			else if (c[i] == '\r')
				buf.append("\\r");
			else if (c[i] == '\'')
				buf.append("\\'");
			else if (c[i] == '"')
				buf.append("\\\"");
			else if (c[i] == '%')
				buf.append("\\%");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}

	public static String str2alert(String s) {
		if (s == null)
			return null;
		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '\n')
				buf.append("\\n");
			else if (c[i] == '\t')
				buf.append("\\t");
			else if (c[i] == '"')
				buf.append("'");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}

	public static String java2msg(String s) {
		if (s == null)
			return null;
		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '\n')
				buf.append("\\n");
			else if (c[i] == '\t')
				buf.append("\\t");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}

	public static String java2html(String s) {
		if (s == null)
			return null;
		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '&')
				buf.append("&amp;");
			else if (c[i] == '<')
				buf.append("&lt;");
			else if (c[i] == '>')
				buf.append("&gt;");
			else if (c[i] == '"')
				buf.append("&quot;");
			else if (c[i] == '\'')
				buf.append("&#039;");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}

	public static String toOneLine(String string) {
		if (string == null)
			return null;
		else
			return string.replace('\n', ' ');
	}

	public static String stripHyphen(String org) throws Exception {
		if (org != null)
			// exception handling
			for (;;) {
				int index = org.indexOf("-");
				if (index >= 0) {
					org = org.substring(0, index) + org.substring(index + 1);
				} else {
					break;
				}
			}
		return org;
	}

	public static String print(String value, String format) {
		Object o = value;
		if (o == null)
			return "";
		if (format == null)
			return o.toString();
		try {
			if (o instanceof String) {
				String s = (String) o;
				int j = 0;
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < format.length(); i++) {
					if (format.charAt(i) == '#') {
						if (j >= s.length())
							return sb.toString();
						sb.append(s.charAt(j++));
					} else {
						sb.append(format.charAt(i));
					}
				}
				return sb.toString();
			}
			if (o instanceof Number || o instanceof BigDecimal) {
				DecimalFormat df = new DecimalFormat(format);
				return df.format(o);
			}
		} catch (Exception e) {
		}
		return o.toString();
	}

	public static String getCalcStr(String str, int sLoc, int eLoc) {
		byte[] bystStr = null;
		String rltStr = "";
		try {
			bystStr = str.getBytes();
			rltStr = new String(bystStr, sLoc, eLoc - sLoc);
		} catch (Exception e) {
			return str;
		}
		return rltStr;
	}

	public static String makeRepeatString(String str, int repeateCount) {
		StringBuffer resultStr = new StringBuffer();
		for (int inx = 0; inx < repeateCount; inx++) {
			resultStr.append(str);
		}
		return resultStr.toString();
	}

	public static String quoteReplacement(String s) {
		if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1))
			return s;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\') {
				sb.append('\\');
				sb.append('\\');
			} else if (c == '$') {
				sb.append('\\');
				sb.append('$');
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String quoteReplacement(String s, char ch) {
		if (s.indexOf(ch) == -1)
			return s;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == ch) {
				sb.append('\\');
				sb.append(ch);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String replace(String strTarget, String strSearch, String strReplace) {
		String result = null;
		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strSearch);
			if (begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + strSearch.length();
				strBuf.append(strCheck.substring(0, begin));
				strBuf.append(strReplace);
				strCheck = strCheck.substring(end);
			}
		}
		result = strBuf.toString();
		return result;
	}

	public static String insertDelimToString(String org, char delim) {
		StringBuffer sb = new StringBuffer();

		char[] chars = org.toCharArray();
		for (int inx = 0; inx < chars.length; inx++) {
			if (inx == 0) {
				if (chars[inx] == delim) {
					sb.append(delim).append(chars[inx]);
				} else {
					sb.append(chars[inx]);
				}
			} else if (inx == chars.length - 1) {
				if (chars[inx - 1] == delim) {
					sb.append(delim).append(chars[inx]);
				} else {
					sb.append(chars[inx]);
				}
			} else {
				if (chars[inx] == delim && chars[inx - 1] != delim) {
					sb.append(delim).append(chars[inx]);
				} else {
					sb.append(chars[inx]);
				}
			}
		}

		return sb.toString();
	}
}
