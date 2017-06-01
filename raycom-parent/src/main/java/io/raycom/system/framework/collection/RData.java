package io.raycom.system.framework.collection;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RData extends RDataProtocol {
	private static final long serialVersionUID = 1L;

	public RData(String name) {
		this.name = name;
	}

	public RData(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public RData(int initialCapacity) {
		super(initialCapacity);
	}

	public RData() {
		super();
	}

	public RData(@SuppressWarnings("rawtypes") Map m) {
		super(m);
	}

	public RData(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	@SuppressWarnings("unchecked")
	public void set(Object key, Object value) {
		super.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public void setString(Object key, String value) {
		super.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public void setInt(Object key, int value) {
		Integer integer = new Integer(value);
		super.put(key, integer);
	}

	@SuppressWarnings("unchecked")
	public void setDouble(Object key, double value) {
		Double dou = new Double(value);
		super.put(key, dou);
	}

	@SuppressWarnings("unchecked")
	public void setFloat(Object key, float value) {
		Float flo = new Float(value);
		super.put(key, flo);
	}

	@SuppressWarnings("unchecked")
	public void setLong(Object key, long value) {
		Long lon = new Long(value);
		super.put(key, lon);
	}

	@SuppressWarnings("unchecked")
	public void setShort(Object key, short value) {
		Short shor = new Short(value);
		super.put(key, shor);
	}

	@SuppressWarnings("unchecked")
	public void setBoolean(Object key, boolean value) {
		Boolean bool = new Boolean(value);
		super.put(key, bool);
	}

	@SuppressWarnings("unchecked")
	public void setBigDecimal(Object key, BigDecimal value) {
		super.put(key, value);
	}

	public BigDecimal getBigDecimal(Object key) {
		Object o = get(key);
		if (o == null) {
			return new BigDecimal(0);
		} else {
			return (BigDecimal) o;
		}
	}

	public Object get(Object key) {
		Object o = super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return "";
			} else {
				return null;
			}
		} else {
			return o;
		}
	}

	public int getInt(Object key) {
		Object o = super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return 0;
			} else {
				throw new RuntimeException("[RuntimeException in RData] Key(" + key + ") does not exist in "
						+ this.name + " RData or Key(" + key + ")'s value is null.");
			}
		} else {
			Class classType = o.getClass();
			if (classType == Integer.class) {
				return ((Integer) o).intValue();
			} else if (classType == Short.class) {
				return ((Short) o).shortValue();
			}

			if (classType == String.class) {
				try {
					return Integer.parseInt(o.toString());
				} catch (Exception e) {
					throw new RuntimeException(
							"[RuntimeException in RData] Value Type(int) does not match : It's type is not int.");
				}
			}else{
				try {
					return Integer.parseInt(String.valueOf(o));
				} catch (NumberFormatException e) {
					throw new RuntimeException(
							"[RuntimeException in RData] Value Type(int) does not match : It's type is not int.");
				} 
			}
		}
	}

	public double getDouble(Object key) {
		Object o = super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return 0.0;
			} else {
				throw new RuntimeException("[RuntimeException in RData] Key(" + key + ") does not exist in "
						+ this.name + " RData or Key(" + key + ")'s value is null.");
			}
		} else {
			Class classType = o.getClass();
			if (classType == Double.class) {
				return ((Double) o).doubleValue();
			} else if (classType == Float.class) {
				return ((Float) o).floatValue();
			}

			if (classType == String.class || classType == BigDecimal.class) {
				try {
					return Double.parseDouble(o.toString());
				} catch (Exception e) {
					throw new RuntimeException(
							"[RuntimeException in RData] Value Type(double) does not match : It's type is not double.");
				}
			}
			throw new RuntimeException(
					"[RuntimeException in RData] Value Type(double) does not match : It's type is not double.");
		}
	}

	public float getFloat(Object key) {
		Object o = super.get(key);
		if (o == null) {
			if (this.nullToInitialize) {
				return (float) 0.0;
			} else {
				throw new RuntimeException("[RuntimeException in RData] Key(" + key + ") does not exist in "
						+ this.name + " RData or Key(" + key + ")'s value is null.");
			}
		} else {
			Class classType = o.getClass();
			if (classType == Float.class) {
				return ((Float) o).floatValue();
			}

			if (classType == String.class || classType == BigDecimal.class) {
				try {
					return Float.parseFloat(o.toString());
				} catch (Exception e) {

					throw new RuntimeException(
							"[RuntimeException in RData] Value Type(float) does not match : It's type is not float.");
				}
			}

			throw new RuntimeException(
					"[RuntimeException in RData] Value Type(float) does not match : It's type is not float.");
		}
	}

	public long getLong(Object key) {
		Object o = super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return 0;
			} else {
				throw new RuntimeException("[RuntimeException in RData] Key(" + key + ") does not exist in "
						+ this.name + " RData or Key(" + key + ")'s value is null.");
			}
		} else {
			Class classType = o.getClass();
			if (classType == Long.class) {
				return ((Long) o).longValue();
			} else if (classType == Integer.class) {
				return ((Integer) o).intValue();
			} else if (classType == Short.class) {
				return ((Short) o).shortValue();
			}

			if (classType == String.class) {
				try {
					return Long.parseLong(o.toString());
				} catch (Exception e) {

					throw new RuntimeException(
							"[RuntimeException in RData] Value Type(long) does not match : It's type is not long.");
				}
			}

			throw new RuntimeException(
					"[RuntimeException in RData] Value Type(long) does not match : It's type is not long.");
		}
	}

	public short getShort(Object key) {
		Object o = super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return 0;
			} else {
				throw new RuntimeException("[RuntimeException in RData] Key(" + key + ") does not exist in "
						+ this.name + " RData or Key(" + key + ")'s value is null.");
			}
		} else {
			Class classType = o.getClass();
			if (classType == Short.class) {
				return ((Short) o).shortValue();
			}

			if (classType == String.class) {
				try {
					return Short.parseShort(o.toString());
				} catch (Exception e) {

					throw new RuntimeException(
							"[RuntimeException in RData] Value Type(short) does not match : It's type is not short.");
				}
			}

			throw new RuntimeException(
					"[RuntimeException in RData] Value Type(short) does not match : It's type is not short.");
		}
	}

	public boolean getBoolean(Object key) {
		Object o = super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return false;
			} else {

				throw new RuntimeException("[RuntimeException in RData] Key(" + key + ") does not exist in "
						+ this.name + " RData or Key(" + key + ")'s value is null.");
			}
		} else {
			if (o.getClass().isInstance(new Boolean(true))) {
				return ((Boolean) o).booleanValue();
			}

			if (o.getClass().isInstance(new String())) {
				try {
					return Boolean.valueOf(o.toString()).booleanValue();
				} catch (Exception e) {

					throw new RuntimeException(
							"[RuntimeException in RData] Value Type(boolean) does not match : It's type is not boolean.");
				}
			}

			throw new RuntimeException(
					"[RuntimeException in RData] Value Type(boolean) does not match : It's type is not boolean.");
		}
	}

	/**
	 * 
	 * @param key - Object
	 * @return String
	 */
	public String getString(Object key) {
		Object o = super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return "";
			} else {
				return null;
			}
		} else {
			return o.toString();
		}
	}

	/**
	 * 
	 * @return String
	 */
	public String toString() {
		int max = super.size() - 1;
		StringBuffer buf = new StringBuffer();

		Set keySet = super.keySet();
		Iterator keys = keySet.iterator();

		buf.append("\t-----------------[RData Result]------------------");
		buf.append("\n\t\t   KEY\t\t|\t  VALUE");
		buf.append("\n\t-------------------------------------------------");

		for (int i = 0; i <= max; i++) {
			Object o = keys.next();
			if (o == null) {
				buf.append("");
			} else {
				String str = o.toString();
				if (str.length() < 6) {
					buf.append("\n\t  " + o + "\t\t\t|    " + this.getString(o));
				} else if (str.length() < 14) {
					buf.append("\n\t  " + o + "\t\t|    " + this.getString(o));
				} else if (str.length() < 22) {
					buf.append("\n\t  " + o + "\t|    " + this.getString(o));
				} else {
					buf.append("\n\t  " + o + "|    " + this.getString(o));
				}
			}// else if
		}// end for
		buf.append("\n\t-------------------------------------------------");
		return buf.toString();
	}
	
	public RData clone() {
        HashMap result = (HashMap)super.clone();
        RData rdata = new RData();
        rdata.putAll(result);
        return rdata;
    }
}
