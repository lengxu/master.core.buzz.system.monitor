package cn.uyun.dao.es.impl;

import cn.uyun.dao.es.EsOperation;
import com.alibaba.fastjson.JSON;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * Created by 吴晗 on 2018/8/29.
 */
@Repository
public class EsOperationImpl implements EsOperation {
	private static final Logger LOGGER = LoggerFactory.getLogger(EsOperationImpl.class);

	@Autowired
	private JestClient jestClient;

	public synchronized String createIndex(String indexName) {
		try {
			JestResult jestResult = jestClient.execute(new CreateIndex.Builder(indexName).build());
//			if(jestResult.getResponseCode() == 200){
//				System.out.println("创建索引 "+indexName+" 成功!");
//			}
			return jestResult.getJsonString();
		} catch (Exception e) {
			LOGGER.error("创建索引失败，" + e);
		}
		return null;
	}

	public JestResult queryDocumentsById(String indexName, String type, String id) {
		//构建Search对象
		Search search = new Search.Builder("size=10000")
				.addIndex(indexName)
				.addType(type)
				.build();
		try {
			SearchResult searchResult = jestClient.execute(search);
			System.out.println(searchResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * indexNames(多个索引的集合)
	 * type：索引类型
	 * parameters：查询条件
	 */
	public SearchResult queryDocuments(LinkedHashSet indexNames, String type, String queryString, int size) {
		LOGGER.info("=============================================================================");
		LOGGER.info("index："+ JSON.toJSONString(indexNames));
		LOGGER.info("type："+ type);
		LOGGER.info("查询条件："+ queryString);
		//构建Search对象
		Search.Builder builder = new Search.Builder(queryString).addIndex(indexNames);
		Search search = null;
		if(type == null){
			search = builder.setParameter("size", size).build();
		}else {
			search = builder.addType(type)
					.setParameter("size", size)
					.build();
		}

		try {
			return jestClient.execute(search);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SearchResult queryDocumentsByPage(int pageSize, int pageNum, LinkedHashSet indexNames, String type, String queryString) {
		Search.Builder builder = new Search.Builder(queryString);

		return null;
	}

}
