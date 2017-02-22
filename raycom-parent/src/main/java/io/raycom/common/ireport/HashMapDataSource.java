/*
 * @(#):HashMapDataSource.java2010-1-22上午09:48:36csq Exp $
 * $Revision: 
 * $Date: 2010-1-22上午09:48:36
 * -----------------------------------------------------------------------------
 * File  Name  :HashMapDataSource.java
 * Description : 
 * Special Logics :
 * @author caoshengquan, caosq1984@lgcns.com
 *------------------------------------------------------------------------------
 * History :
 *  DATE              AUTHOR            DESCRIPTION
 * -----------     -------------     -------------------
 * 2010-1-22         caoshengquan       Initial Release
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

public class HashMapDataSource {

	RMultiData mData = new RMultiData();

	public HashMapDataSource() {
	}

	public HashMapDataSource(RMultiData oIn) {
		this.mData = oIn;
	}

	public JRDataSource createReportDataSource() {
		JRMapMultiData dataSource;
		dataSource = new JRMapMultiData(this.mData);
		return dataSource;
	}
}
