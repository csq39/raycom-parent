/*   
 * Copyright (c) 2015-2020 CSVALUE.COM All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */     
package io.raycom.common.utils.dao;

import io.raycom.system.framework.collection.RData;
import io.raycom.system.framework.collection.RMultiData;

import java.util.List;

public class DaoUtil {

	/**
	 * @Description:
	 * 		把list类型的数据转换Rmutildate类型
	 * @param data
	 * @return  CMultiData
	 * @throws
	 */
	public static RMultiData ConvertList2CMulti(List<RData> data) {
		RMultiData mData = new RMultiData();

		for (RData element : data)
			if (element != null)
				mData.addRData(element);

		return mData;
	}

}
 