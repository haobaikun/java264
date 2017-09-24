package cn.itcast.solrjd.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.itcast.solrjd.pojo.ResultModel;

public interface ProductDao {

	/**
	 * 根据查询条件查询数据
	 * @param query
	 * @return
	 */
	public ResultModel findProductList(SolrQuery query) throws Exception;
}
