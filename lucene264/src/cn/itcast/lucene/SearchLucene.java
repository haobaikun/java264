package cn.itcast.lucene;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.packed.DirectReader;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class SearchLucene {

	@Test
	public void test() throws Exception {
		//1.创建分词器对象
//		StandardAnalyzer analyzer = new StandardAnalyzer();
		//配置中文分词器
		IKAnalyzer analyzer = new IKAnalyzer();
		//2.创建一个搜索解析器对象Queryparser(),需要分词器对象
		//参数说明：1.默认搜索域名；2.指定分词器对象
		QueryParser parser = new QueryParser("name",analyzer);
		//3.根据这个解析器对象创建查询条件对象Query
		//如果创建这个查询条件对象没有指定搜索域，那么久根据默认搜索域来搜索
		//如果创建这个查询条件对象指定了搜索域，那么或根据指定的搜索域来搜索覆盖默认搜索域名
		Query query = parser.parse("desc:项目");
		//4.创建索引库地址这个流对象
		Directory directory = FSDirectory.open(new File("D:\\index264"));
		//5.根据这个流读出索引库(索引和文档)
		IndexReader indexReader = DirectoryReader.open(directory);
		//6.根据这个读的这个对象创建IndexSearch搜索对象(索引和文档),得到的值里面有文档和索引
		IndexSearcher searcher = new IndexSearcher(indexReader);
		//7.根据这个索引对象和查询条件对象来查询：索引-->返回结果集TopDocs,得到的值里面有坐标
		//参数说明：1.根据查询的条件对象；2.查询的每页显示的记录数，自己定
		TopDocs topDocs = searcher.search(query, 2);
		//总记录数
		System.err.println("总记录数="+topDocs.totalHits);
		//8.根据这个结果集获取坐标：数组形式
		ScoreDoc[] docs = topDocs.scoreDocs;
		//9.遍历坐标
		for (ScoreDoc scoreDoc : docs) {
			System.out.println("--------------");
			//10.获取每个坐标下的文档的id
			int docId = scoreDoc.doc;
			//11.根据文档的ID，根据IndexSearch对象来查询文档
			Document doc = searcher.doc(docId);
			//12.打印文档数据
			System.err.println("----id-----"+doc.get("id"));
			System.err.println("----name-----"+doc.get("name"));
			System.err.println("----price-----"+doc.get("price"));
			System.err.println("----pic-----"+doc.get("pic"));
		}
		//13.释放资源
		indexReader.close();
	}

}
