package cn.uyun.dao.es;

import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult;

import java.util.LinkedHashSet;

/**
 * Created by 吴晗 on 2018/8/29.
 */
public interface EsOperation {
	public abstract String createIndex(String indexName);

	public abstract JestResult queryDocumentsById(String indexName, String type, String id);

	public abstract SearchResult queryDocuments(LinkedHashSet indexNames, String type, String queryString, int size);

	//分页查询
	public abstract SearchResult queryDocumentsByPage(int pageSize, int pageNum, LinkedHashSet indexNames, String type, String queryString);
}
