package cn.itcast.lucene;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.itcast.dao.BookDaoImpl;
import cn.itcast.pojo.Book;


public class IndexLucene {

	@Test
	public void test() throws Exception {
		//1.采集数据
		BookDaoImpl daoImpl = new BookDaoImpl();
		List<Book> list = daoImpl.findBookList();
		//创建文档集合
		List<Document> docList = new ArrayList<>();
		for (Book book : list) {
			//2.创建文档对象
			Document doc = new Document();
			//3.创建域field对象，把数据放到filed域中
			//参数说明：1.指定域名；2.域值；是否存储到索引库，默认为存储
			Field idField = new TextField("id", book.getId()+"", Store.YES);
			Field nameField = new TextField("name", book.getName()+"", Store.YES);
			Field priceField = new TextField("price", book.getPrice()+"", Store.YES);
			Field picField = new TextField("pic", book.getPic()+"", Store.YES);
			Field descField = new TextField("desc", book.getDesc()+"", Store.YES);
			//4.把域加载到文档中
			doc.add(idField);
			doc.add(nameField);
			doc.add(priceField);
			doc.add(picField);
			doc.add(descField);
			//把文档加载到集合中
			docList.add(doc);
		}
		//5.创建分词对象
//		StandardAnalyzer analyzer = new StandardAnalyzer();
		IKAnalyzer analyzer = new IKAnalyzer();
		
		//6.创建指定的索引库地址流对象,返回一个流对象
		Directory directory = FSDirectory.open(new File("D:\\index264"));
		//7.创建写入索引库的配置对象，配置Lucene的版本和分词器
		//参数说明：1.指定Lucene的版本；2.指定写入索引库的分词器
		IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		//8.创建写入索引库对象IndexWriter(1.指定索引库地址和对象，2.指定写入索引库的配置对象)
		IndexWriter indexWriter = new IndexWriter(directory, writerConfig);
		//9.根据这个写入索引库的对象把文档写入索引库
		for (Document document : docList) {
			indexWriter.addDocument(document);
		}
		//10.释放资源
		indexWriter.close();
	}

}
