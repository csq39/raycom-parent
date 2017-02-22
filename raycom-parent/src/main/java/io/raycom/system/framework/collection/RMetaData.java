package io.raycom.system.framework.collection;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class RMetaData extends LinkedHashMap {
	private static final long serialVersionUID = 4385734035927398343L;

	private String name;

	/**
	 * Constructor for RMetaData.
	 */
	public RMetaData(String name) {
		this.name = name;
	}

	/**
	 * Constructor for RMetaData.
	 * 
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public RMetaData(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Constructor for RMetaData.
	 * 
	 * @param initialCapacity
	 */
	public RMetaData(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Constructor for RMetaData.
	 */
	public RMetaData() {
		super();
	}

	/**
	 * 
	 * @param keyName
	 * @return int
	 */
	public int getType(String keyName) {
		InnerMetaData metaData = (InnerMetaData) this.get(keyName);
		return metaData.getType();
	}

	/**
	 * 
	 * @param keyName
	 * @return int
	 */
	public int getSize(String keyName) {
		InnerMetaData metaData = (InnerMetaData) this.get(keyName);
		return metaData.getSize();
	}

	/**
	 * 
	 * @param keyName
	 * @return int
	 */
	public int getScale(String keyName) {
		InnerMetaData metaData = (InnerMetaData) this.get(keyName);
		return metaData.getScale();
	}

	/**
	 * 
	 * @param keyName
	 * @param type
	 */
	public void setType(String keyName, int type) {
		Object obj = this.get(keyName);
		InnerMetaData metaData = null;

		if (obj == null) {
			metaData = new InnerMetaData(type, 0, 0);
			this.put(keyName, metaData);
		} else {
			metaData = (InnerMetaData) obj;
		}
		metaData.setType(type);
	}

	/**
	 * 
	 * @param keyName
	 * @param size
	 */
	public void setSize(String keyName, int size) {
		Object obj = this.get(keyName);
		InnerMetaData metaData = null;

		if (obj == null) {
			metaData = new InnerMetaData(0, size, 0);
			this.put(keyName, metaData);
		} else {
			metaData = (InnerMetaData) obj;
		}
		metaData.setSize(size);
	}

	/**
	 * 
	 * @param keyName
	 * @param scale
	 */
	public void setScale(String keyName, int scale) {
		Object obj = this.get(keyName);
		InnerMetaData metaData = null;

		if(obj == null) {
			metaData = new InnerMetaData(0, 0, scale);
			this.put(keyName, metaData);
		}else {
			metaData = (InnerMetaData) obj;
		}
		metaData.setScale(scale);
	}

	/**
	 * 
	 * @param keyName
	 * @param metaString
	 */
	public void setMetaString(String keyName, String metaString) {
		Object obj = (InnerMetaData) this.get(keyName);
		InnerMetaData metaData = null;

		if (obj == null) {
			metaData = new InnerMetaData(metaString);
			this.put(keyName, metaData);
		} else {
			metaData = (InnerMetaData) obj;
		}
		metaData.setMetaString(metaString);
	}

	/**
	 * 
	 * @param keyName
	 * @return String
	 */
	public String getMetaString(String keyName) {
		InnerMetaData metaData = (InnerMetaData) this.get(keyName);
		return metaData.getMetaString();
	}

	/**
	 * name getter
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * return한다.
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
				buf.append("\n\t  " + o + "\t\t\t|    " + this.getMetaString(o.toString()));
			}// else if
		}// end for
		buf.append("\n\t-------------------------------------------------");
		return buf.toString();
	}

	class InnerMetaData implements Serializable {

		private static final long serialVersionUID = -1952104068051941485L;

		private int type;

		private int size;

		private int scale;

		InnerMetaData(int type, int size, int scale) {
		}

		InnerMetaData(String metaString) {
			setMetaString(metaString);
		}

		public void setMetaString(String metaString) {
			String[] elements = metaString.split("\\|");

			if (elements == null || elements.length != 3) {

				throw new RuntimeException("[COR_COL_009]" + metaString
						+ " is not wrong format. The format is [type|size|sacle].");
			} else {
				try {
					setType(Integer.parseInt(elements[0]));
					setSize(Integer.parseInt(elements[1]));
					setScale(Integer.parseInt(elements[2]));
				} catch (NumberFormatException nfe) {

					throw new RuntimeException("[COR_COL_008]" + metaString
							+ " is not wrong format. The value of type, size and scale should be an integer. : " + nfe);
				}
			}
		}

		public String getMetaString() {
			return getType() + "|" + getSize() + "|" + getScale();
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			if (size < 0) {

				throw new RuntimeException("[COR_COL_010]The value of size[input: " + size
						+ "] should be greater than 0.");
			}
			this.size = size;
		}

		public int getScale() {
			return scale;
		}

		public void setScale(int scale) {
			this.scale = scale;
		}

	}
}
