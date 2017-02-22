/*
 * @(#):JRMapMultiData.java2010-1-25上午10:46:55csq Exp $
 * $Revision: 
 * $Date: 2010-1-25上午10:46:55
 * -----------------------------------------------------------------------------
 * File  Name  :JRMapMultiData.java
 * Description : 
 * Special Logics :
 * @author caoshengquan, caosq1984@lgcns.com
 *------------------------------------------------------------------------------
 * History :
 *  DATE              AUTHOR            DESCRIPTION
 * -----------     -------------     -------------------
 * 2010-1-25         caoshengquan       Initial Release
 *------------------------------------------------------------------------------
 * Copyright(c) 2007 LG CNS,  All rights reserved.
 *
 * NOTICE !    You can copy or redistribute this code freely,
 * but you should not remove the information about the copyright notice
 * and the author.
 *
 */
package io.raycom.common.ireport;

import io.raycom.system.framework.collection.RMultiData;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;

public class JRMapMultiData implements JRDataSource {

	/**
	 *
	 */
	private RMultiData mData = new RMultiData();
	private int i = -1;//max index 
	private int currentRecord = -1;

	/**
	 *
	 */
	public JRMapMultiData(RMultiData col) {
		mData = col;

		if (mData != null && mData.getMaxDataCount() > 0) {
			i = mData.getMaxDataCount();
			currentRecord = 0;

		}
	}

	/**
	 *
	 */
	public boolean next() {
		boolean hasNext = false;

		if (currentRecord != -1) {
			hasNext = currentRecord < i;

			if (hasNext) {
				currentRecord++;
			}
		}

		return hasNext;
	}

	/**
	 *
	 */
	public Object getFieldValue(JRField field) {
		Object value = null;

		if (currentRecord != -1) {
			value = mData.get(field.getName(), currentRecord - 1);
		}

		return value;
	}

	/**
	 *
	 */
	public void moveFirst() {
		if (currentRecord != -1) {
			currentRecord = 0;
		}
	}

	/**
	 * Returns the total number of records/maps that this data source
	 * contains.
	 * 
	 * @return the total number of records of this data source
	 */
	public int getRecordCount() {
		return currentRecord == -1 ? 0 : i;
	}

	/**
	 * Clones this data source by creating a new instance that reuses the same
	 * underlying map collection.
	 * 
	 * @return a clone of this data source
	 */
	public JRMapMultiData cloneDataSource() {
		return new JRMapMultiData(mData);
	}

}
