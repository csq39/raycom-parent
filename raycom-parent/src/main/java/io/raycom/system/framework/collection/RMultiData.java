package io.raycom.system.framework.collection;

import io.raycom.common.utils.string.CStringConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RMultiData extends RDataProtocol {
	private static final long serialVersionUID = 1L;

	private int field_index = 0;

	HashMap entityKey = null;

	/**
	 * Constructor for RMultiData.
	 * 
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public RMultiData(String name) {
		this.name = name;
	}

	/**
	 * Constructor for RMultiData.
	 * 
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public RMultiData(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Constructor for RMultiData.
	 * 
	 * @param initialCapacity
	 */
	public RMultiData(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Constructor for RMultiData.
	 */
	public RMultiData() {
		super();
	}

	/**
	 * Constructor for RMultiData.
	 * 
	 * @param m
	 */
	public RMultiData(Map m) {
		super(m);
	}

	/**
	 * Constructor for RMultiData.
	 * 
	 * @param initialCapacity
	 * @param loadFactor
	 * @param accessOrder
	 */
	public RMultiData(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	/**
	 * 
	 * @param key String
	 * @param index int
	 * @param replaceValue Object
	 */
	public void modify(Object key, int index, Object replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, replaceValue);
	}

	/**
	 * @param key String
	 * @param index int
	 * @param replaceValue int
	 */
	public void modifyInt(Object key, int index, int replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, new Integer(replaceValue));
	}

	/**
	 * @param key String
	 * @param index int
	 * @param replaceValue String
	 */
	public void modifyString(Object key, int index, String replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, replaceValue);
	}

	/**
	 * 해당 key값에 해당하는 index번째의 Value를 파라미터로 전달된 value로 Replace한다.
	 * 
	 * @param key String
	 * @param index int
	 * @param replaceValue double
	 */
	public void modifyDouble(Object key, int index, double replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, new Double(replaceValue));
	}

	/**
	 * @param key String
	 * @param index int
	 * @param replaceValue double
	 */
	public void modifyFloat(Object key, int index, float replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {
			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, new Float(replaceValue));
	}

	/**
	 * @param key String
	 * @param index int
	 * @param replaceValue long
	 */
	public void modifyLong(Object key, int index, long replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, new Long(replaceValue));
	}

	/**
	 * @param key String
	 * @param index int
	 * @param replaceValue short
	 */
	public void modifyShort(Object key, int index, short replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, new Short(replaceValue));
	}

	/**
	 * @param key String
	 * @param index int
	 * @param replaceValue boolean
	 */
	public void modifyBoolean(Object key, int index, boolean replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, new Boolean(replaceValue));
	}

	/**
	 * @param key String
	 * @param index int
	 * @param replaceValue BigDecimal
	 */
	public void modifyBigDecimal(Object key, int index, BigDecimal replaceValue) {
		if (!super.containsKey(key)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
					+ ") does not exist in RMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList) this.get(key);
		int valueSize = arrayList.size();
		if (!(valueSize > index)) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") of Key(" + key
					+ ") exceeds size(" + (valueSize - 1) + ") of RMultiData");
		}
		arrayList.set(index, replaceValue);
	}

	/**
	 * @param key - Object
	 * @param value - Object
	 */
	public void add(Object key, Object value) {
		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(value);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(value);
		}
	}

	/**
	 * @param key - Object
	 * @param value - String
	 */
	public void addString(Object key, String value) {
		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(value);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(value);
		}
	}

	/**
	 * @param key - Object
	 * @param value - int
	 */
	public void addInt(Object key, int value) {
		Integer valueInt = new Integer(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueInt);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueInt);
		}
	}

	/**
	 * @param key - Object
	 * @param value - double
	 */
	public void addDouble(Object key, double value) {
		Double valueDouble = new Double(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueDouble);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueDouble);
		}
	}

	/**
	 * @param key - Object
	 * @param value - float
	 */
	public void addFloat(Object key, float value) {
		Float valueFloat = new Float(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueFloat);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueFloat);
		}
	}

	/**
	 * @param key - Object
	 * @param value - long
	 */
	public void addLong(Object key, long value) {
		Long valueLong = new Long(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueLong);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueLong);
		}
	}

	/**
	 * @param key - Object
	 * @param value - short
	 */
	public void addShort(Object key, short value) {
		Short valueShort = new Short(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueShort);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueShort);
		}
	}

	/**
	 * @param key - Object
	 * @param value - boolean
	 */
	public void addBoolean(Object key, boolean value) {
		Boolean valueBoolean = new Boolean(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueBoolean);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueBoolean);
		}
	}

	/**
	 * @param key - Object
	 * @param value - BigDecimal
	 */
	public void addBigDecimal(Object key, BigDecimal value) {
		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(value);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(value);
		}
	}

	/**
	 * @param key - Object
	 * @param index - int
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimal(Object key, int index) {
		Object o = getObject(key, index);
		if (o == null) {
			return new BigDecimal(0);
		} else {
			return (BigDecimal) o;
		}
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimal(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getBigDecimal(key, index);
	}

	private Object getObject(Object key, int index) {
		Object o = null;
		ArrayList arrayList = (ArrayList) super.get(key);

		if (arrayList == null) {
			if (this.nullToInitialize) {
				return "";
			} else {

				throw new RuntimeException("[RuntimeException in RMultiData] Key(" + key
						+ ") does not exist in RMultiData(" + this.name + ") ");
			}
		}

		try {
			if (index >= arrayList.size()) {
				return null;
			}
			o = arrayList.get(index);

			if (o == null && this.nullToInitialize) {
				return "";
			}
		} catch (IndexOutOfBoundsException ioe) {

			throw new RuntimeException("[RuntimeException in RMultiData] Index(" + index + ") in RMultiData("
					+ this.name + ") is out of Bounds.");
		}
		return o;
	}

	/**
	 * @param key - Object
	 * @param index - int
	 * @return Object
	 */
	public Object get(Object key, int index) {
		return getObject(key, index);
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return Object
	 */
	public Object get(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getObject(key, index);
	}

	/**
	 * @param key - Object
	 * @param index - int
	 * @return int
	 */
	public int getInt(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize()) {
				return 0;
			} else {

				throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key + ") is null");
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

					throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
							+ ") Type(int) does not match : It's type is not int");
				}
			}

			throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
					+ ") Type(int) does not match : It's type is not int");
		}
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return int
	 */
	public int getInt(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getInt(key, index);
	}

	/**
	 * @param key - Object
	 * @param index - int
	 * @return double
	 */
	public double getDouble(Object key, int index) {
		Object o = getObject(key, index);
		if (o == null) {
			if (isNullToInitialize()) {
				return 0.0;
			} else {

				throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key + ") is null");
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

					throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
							+ ") Type(double) does not match : It's type is not double");
				}
			}

			throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
					+ ")  Type(double) does not match : It's type is not double");
		}
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return double
	 */
	public double getDouble(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getDouble(key, index);
	}

	/**
	 * @param key - Object
	 * @param index - int
	 * @return float
	 */
	public float getFloat(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize()) {
				return (float) 0.0;
			} else {

				throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key + ") is null");
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

					throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
							+ ") Type(float) does not match : It's type is not float");
				}
			}

			throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
					+ ") Type(float) does not match : It's type is not float");
		}
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return float
	 */
	public float getFloat(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getFloat(key, index);
	}

	/**
	 * @param key - Object
	 * @param index - int
	 * @return long
	 */
	public long getLong(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize()) {
				return 0;
			} else {

				throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key + ") is null");
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

					throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
							+ ") Type(long) does not match : It's type is not long");
				}
			}

			throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
					+ ") Type(long) does not match : It's type is not long");
		}
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return long
	 */
	public long getLong(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getLong(key, index);
	}

	/**
	 * @param key - Object
	 * @param index - int
	 * @return short
	 */
	public short getShort(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize()) {
				return 0;
			} else {

				throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key + ")  is null");
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

					throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
							+ ") Type(short) does not match : It's type is not short");
				}
			}

			throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
					+ ") Type(short) does not match : It's type is not short");
		}
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return short
	 */
	public short getShort(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getShort(key, index);
	}

	/**
	 * @param key - Object
	 * @param index - int
	 * @return boolean
	 */
	public boolean getBoolean(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize()) {
				return false;
			} else {

				throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key + ") is null");
			}
		} else {
			Class classType = o.getClass();
			if (classType == Boolean.class) {
				return ((Boolean) o).booleanValue();
			}
			if (classType == String.class) {
				try {
					return Boolean.valueOf(o.toString()).booleanValue();
				} catch (Exception e) {

					throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
							+ ") Type(short) does not match : It's type is not short");
				}
			}

			throw new RuntimeException("[RuntimeException in RMultiData] Value of the Key(" + key
					+ ") Type(short) does not match : It's type is not short");
		}
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return boolean
	 */
	public boolean getBoolean(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getBoolean(key, index);
	}

	/**
	 * @param key String
	 * @param index int
	 * @return String
	 */
	public String getString(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize())
				return "";
			else
				return null;
		} else {
			return o.toString();
		}
	}

	/**
	 * @param keyIndex - int
	 * @param index - int
	 * @return String
	 */
	public String getString(int keyIndex, int index) {
		Object key = getKeyWithIndex(keyIndex);
		return getString(key, index);
	}

	/**
	 * @param key RMultiData에 존재하는 key 값
	 * @param index 특정 키 값에 존재하는 index
	 * @return java.lang.Object 삭제된 객체를 반환한다.
	 */
	public Object remove(Object key, int index) {
		if (super.containsKey(key)) {
			return ((ArrayList) super.get(key)).set(index, null);
		} else {
			return null;
		}
	}

	/**
	 * @param index - 삭제하고자 하는 row의 index
	 */
	public void removeRow(int index) {
		Set keySet = super.keySet();
		Iterator iterator = keySet.iterator();

		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			ArrayList al = ((ArrayList) super.get(key));
			al.remove(index);
		}
	}

	/**
	 */
	public int getDataCount(Object key) {
		if (super.containsKey(key)) {
			return ((ArrayList) super.get(key)).size();
		} else {
			return 0;
		}
	}

	/**
	 * @return int
	 */
	public int getDataCount() {
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		if (iterator.hasNext()) {
			String key = iterator.next().toString();
			return ((ArrayList) super.get(key)).size();
		} else {
			return 0;
		}
	}

	/**
	 * @param index int
	 * @return RData
	 */
	public RData getRData(int index) {
		RData singleData = new RData("");
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			Object o = getObject(key, index);
			singleData.put(key, o);
		}
		return singleData;
	}

	/**
	 * @param dataName - String
	 * @param index - int
	 * @return RData
	 */
	public RData getRData(String dataName, int index) {
		RData singleData = new RData(dataName);
		String prefix = dataName + ".";
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			int key_index = key.indexOf(".");
			String realKey = key.substring(key_index + 1);
			if (key.startsWith(prefix)) {
				Object o = getObject(key, index);
				singleData.put(realKey, o);
			}
		}
		return singleData;
	}

	/**
	 * @param data - RData
	 */
	public void addRData(RData data) {
		// RData 기준
		Set tempSet = data.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			if (this.containsKey(key)) {
				int field_size = ((ArrayList) this.get(key)).size();
				if (field_size != field_index) {
					for (int inx = field_size; inx < field_index; inx++) {
						this.add(key, null);
					} // end for
				} // end if
				this.add(key, data.get(key));
			} // end if
			else {
				for (int inx = 0; inx < field_index; inx++) {
					this.add(key, null);
				}
				this.add(key, data.get(key));
			} // end else
		} // end while
		field_index++;

		// RMultiData 기준
		Set thisSet = this.keySet();
		Iterator thisIiterator = thisSet.iterator();
		while (thisIiterator.hasNext()) {
			Object key = thisIiterator.next();
			if (!data.containsKey(key)) {
				int field_size = ((ArrayList) this.get(key)).size();
				for (int inx = field_size; inx < field_index; inx++) {
					this.add(key, null);
				}
			}
		}
	}

	/**
	 * @param dataName - String
	 * @param data - RData
	 */
	public void addRData(String dataName, RData data) {
		int entitySize = 0;
		if (entityKey == null) {
			entityKey = new HashMap(5);
		} else {
			if (entityKey.containsKey(dataName))
				entitySize = ((Integer) entityKey.get(dataName)).intValue();
		}

		// RData 기준
		Set tempSet = data.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			String dataKey = dataName + "." + key;
			if (this.containsKey(dataKey)) {
				int fieldSize = ((ArrayList) this.get(dataKey)).size();
				if (fieldSize != entitySize) {
					for (int inx = fieldSize; inx < entitySize; inx++) {
						this.add(dataKey, null);
					} // end for
				} // end if
				this.add(dataKey, data.get(key));
			} // end if
			else {
				for (int inx = 0; inx < entitySize; inx++) {
					this.add(dataKey, null);
				}
				this.add(dataKey, data.get(key));
			} // end else
		} // end while
		entityKey.put(dataName, new Integer(entitySize + 1));

		// RMultiData 기준
		Set thisSet = this.keySet();
		Iterator thisIiterator = thisSet.iterator();
		while (thisIiterator.hasNext()) {
			String key = (String) thisIiterator.next();
			String keyPrefix = dataName + ".";

			if (key.indexOf(keyPrefix) > -1 && !data.containsKey(key)) {
				int fieldSize = ((ArrayList) this.get(key)).size();
				int entityLength = ((Integer) entityKey.get(dataName)).intValue();
				for (int inx = fieldSize; inx < entityLength; inx++) {
					this.add(key, null);
				}
			}
		}
	}

	/**
	 * @return String
	 */
	public String toString() {
		boolean checkLongString = true;
		StringBuffer buf = new StringBuffer();

		Set keySet = this.keySet();
		int keySize = keySet.size();
		String[] keyStr = new String[keySize];
		keySet.toArray(keyStr);
		buf.append(" [RMultiData Result]-------------");
		buf.append(CStringConverter.makeRepeatString("-", (keySize - 1) * 23 - 1));
		buf.append("|");
		buf.append("\n |{index}| ");
		int keyLoopNumber = 0;
		while (checkLongString) {
			checkLongString = false;
	
			for (int inx = 0; inx < keySize; inx++) {
				int keyLength = keyStr[inx].length();
				int printKeyLength = 0;
				if (keyLength > (keyLoopNumber + 1) * 20) {
					printKeyLength = (keyLoopNumber + 1) * 20;
					checkLongString = true;
				} else {
					if (keyLoopNumber == 0 || keyLength > (keyLoopNumber * 20)) {
						printKeyLength = keyLength;
					} else {
						printKeyLength = 0;
					}
				}
				if (printKeyLength != 0) {
					buf.append(keyStr[inx].substring(keyLoopNumber * 20, printKeyLength));
				}
				if (printKeyLength != 0 && (printKeyLength % 20) == 0) {
					printKeyLength = 20;
				} else {
					buf.append(CStringConverter.makeRepeatString(" ", 20 - (printKeyLength % 20)));
				}
				buf.append(" | ");
			}// end for
			if (checkLongString == false)
				break;
			buf.append("\n |       | ");
			keyLoopNumber++;
		}// end while
		buf.append("\n |-------------------------------");
		buf.append(CStringConverter.makeRepeatString("-", (keySize - 1) * 23 - 1));
		buf.append("|");

		int rowSize = 0;
		for (int inx = 0; inx < keySize; inx++) {
			int rowSizeOfKey = this.getDataCount(keyStr[inx]);
			if (rowSizeOfKey > rowSize) {
				rowSize = rowSizeOfKey;
			}
		}

		for (int inx = 0; inx < rowSize; inx++) {
			buf.append("\n |  ");
			String indexStr = "" + inx;
			buf.append(indexStr);
			buf.append(CStringConverter.makeRepeatString(" ", 5 - indexStr.length()));
			buf.append("| ");
			checkLongString = true;
			int lineLoopNumber = 0;
			while (checkLongString) {
				checkLongString = false;
				for (int jnx = 0; jnx < keySize; jnx++) {
					String tmpValue = this.getString(keyStr[jnx], inx);
					if (tmpValue == null)
						tmpValue = "null";
					int[] uniCode = CUnicodeUtils.getUnicodeLineArray(tmpValue, 20);

					int valueLength = tmpValue.getBytes().length;
					int printValueLength = 0;
					int lastUnicodeNumber = 0;
					int beforeLastUnicodeNumber = 0;
					String printString = "";
					for (int knx = 0; knx < uniCode.length; knx++) {
						if (uniCode[knx] <= lineLoopNumber + 1)
							lastUnicodeNumber++;
					}
					for (int knx = 0; knx < uniCode.length; knx++) {
						if (uniCode[knx] <= lineLoopNumber)
							beforeLastUnicodeNumber++;
					}
					if (valueLength + lastUnicodeNumber > (lineLoopNumber + 1) * 20) {
						printValueLength = (lineLoopNumber + 1) * 20 - lastUnicodeNumber;
						checkLongString = true;
						if (lineLoopNumber == 0) {
							printString = new String(tmpValue.getBytes(), lineLoopNumber * 20, printValueLength
									- lineLoopNumber * 20);
							buf.append(printString);
						}

						else {
							printValueLength += beforeLastUnicodeNumber;
							printString = new String(tmpValue.getBytes(),
									lineLoopNumber * 20 - beforeLastUnicodeNumber, printValueLength - lineLoopNumber
											* 20);
							buf.append(printString);
						}
					} else {
						if (lineLoopNumber == 0 || valueLength + lastUnicodeNumber > (lineLoopNumber * 20)) {
							printValueLength = valueLength;
							if (lineLoopNumber == 0) {
								printString = new String(tmpValue.getBytes(), lineLoopNumber * 20, printValueLength
										- lineLoopNumber * 20 + lastUnicodeNumber);
								buf.append(printString);
							} else {
								printValueLength += lastUnicodeNumber;
								printString = new String(tmpValue.getBytes(), lineLoopNumber * 20 - lastUnicodeNumber,
										printValueLength - lineLoopNumber * 20);
								buf.append(printString);
							}
						} else {
							printValueLength = 0;
						}
					}// end else

					if (printValueLength != 0 && (printValueLength % 20) == 0) {
						printValueLength = 20;
						int unicodeNumber = CUnicodeUtils.countUnicode(printString);
						buf.append(CStringConverter.makeRepeatString(" ", unicodeNumber));
					} else {
						int unicodeNumber = CUnicodeUtils.countUnicode(printString);
						buf.append(CStringConverter.makeRepeatString(" ", unicodeNumber));
						if (printString.length() != 0
								&& CUnicodeUtils.isUnicode(printString.charAt(printString.length() - 1)))
							buf.append(CStringConverter.makeRepeatString(" ", 20 - ((printValueLength) % 20)));
						buf.append(CStringConverter.makeRepeatString(" ", 20 - ((printValueLength) % 20)));
					}
					buf.append(" | ");
				}// end for
				if (checkLongString == false)
					break;
				buf.append("\n |       | ");
				lineLoopNumber++;
			}// end while

			buf.append("\n |-------------------------------");
			buf.append(CStringConverter.makeRepeatString("-", (keySize - 1) * 23 - 1));
			buf.append("|");

			if (inx >= 9 && inx != rowSize - 1) {
				buf.append("\n (RMultiData has more rows. Use LCollectionUtility.toDebugString() method to see all row!!)");
				break;
			}
		}
		buf.append("\n [Total Row Size] = ");
		buf.append("" + rowSize);

		return buf.toString();
	}

	/**
	 * @return int
	 */
	public int getKeyCount() {
		Set tempSet = super.keySet();
		return tempSet.size();
	}

	/**
	 * @param mData - RMultiData
	 */
	public void addRMultiData(RMultiData mData) {
		int size = mData.getMaxDataCount();

		for (int i = 0; i < size; i++) {
			RData data = mData.getRData(i);
			this.addRData(data);

		}
	}

	/**
	 * @param keyIndex
	 * @return int
	 */
	public Object getKeyWithIndex(int keyIndex) {
		Object retObj = null;
		Set tempSet = this.keySet();

		if (keyIndex > tempSet.size()) {
			throw new RuntimeException("[RuntimeException in RMultiData] Key Index(" + keyIndex
					+ ") is greater than RMultiData key size");
		}

		Iterator iterator = tempSet.iterator();
		for (int inx = 0; inx <= keyIndex; inx++) {
			retObj = iterator.next();
		}
		return retObj;
	}

	/**
	 * @return int
	 */
	public int getMaxDataCount() {
		String key = "";
		int maxSize = 0;
		for (int inx = 0; inx < getKeyCount(); inx++) {
			key = (String) getKeyWithIndex(inx);

			int curSize = getDataCount(key);
			if (maxSize <= curSize) {
				maxSize = curSize;
			}
		}
		return maxSize;
	}
}
