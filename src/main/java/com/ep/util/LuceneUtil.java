package com.ep.util;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.lionsoul.jcseg.analyzer.v5x.JcsegAnalyzer5X;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LuceneUtil {
	/**
	 * 创建单个索引
	 * 
	 * @param analyzer
	 * @throws Exception
	 */
	public static void createSingleIndex(Map<String, String> map) throws Exception {
		IndexWriter indexWriter = getIndexWriter();
		LuceneUtil.addSingleDoc(indexWriter, map);
		indexWriter.close();
	}

	/**
	 * 创建sql索引
	 * 
	 * @param analyzer
	 * @throws Exception
	 */
	public static void createSqlIndex(String sql, Map<String, String> map) throws Exception {
		IndexWriter indexWriter = getIndexWriter();
		LuceneUtil.addDocSQL(indexWriter, sql, map);
		indexWriter.close();
	}

	/***
	 * 查询
	 * 
	 * @param searchValue
	 *            输入内容
	 * @param strings【】
	 *            文档属性"qaKeywords","qaQuestion"中查询
	 */
	public static JSONArray search(String searchValue, String[] strings) throws Exception {
		Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
		JcsegAnalyzer5X jcseg = (JcsegAnalyzer5X) analyzer;
		JcsegTaskConfig config = jcseg.getTaskConfig();
		// 追加同义词到分词结果中, 需要在jcseg.properties中配置jcseg.loadsyn=1
		config.setAppendCJKSyn(true);
		// 追加拼音到分词结果中, 需要在jcseg.properties中配置jcseg.loadpinyin=1
		config.setAppendCJKPinyin(true);
		// config.setClearStopwords(true); //设置过滤停止词
		config.setAppendCJKSyn(true); // 设置关闭同义词追加
		// 更多配置, 请查看com.webssky.jcseg.core.JcsegTaskConfig类
		TokenStream stream = null;
		List<String> list = new ArrayList<String>();
		StringBuilder sBuilder = new StringBuilder();
		if (searchValue.length() > 1) {
			try {
				stream = analyzer.tokenStream("myfield", searchValue);
				stream.reset();
				CharTermAttribute offsetAtt = stream.addAttribute(CharTermAttribute.class);
				while (stream.incrementToken()) {
					String string = offsetAtt.toString();
					if (string.length() > 0) {
						sBuilder.append(string);
						list.add(string);
					}
				}
				stream.end();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			sBuilder.append(searchValue);
			// list.add(searchValue);
		}
		Directory dire1 = FSDirectory.open(Paths.get(PropertiesUtil.getProperties_5("/fileUrl.properties", "saveAddress")));
		IndexReader ir = DirectoryReader.open(dire1);
		IndexSearcher searcher = new IndexSearcher(ir);
		QueryParser parser = new MultiFieldQueryParser(strings, analyzer);
		JSONArray jsonList = new JSONArray();
		// Formatter formatter = new SimpleHTMLFormatter("<font color='red'>",
		// "</font>");

		String sendContent = "";
		sendContent = sBuilder.toString();
		Query query = parser.parse(sendContent);
		//Scorer scorer = new QueryScorer(query);
		// Highlighter highlighter = new Highlighter(formatter, scorer);

		TopDocs tops = searcher.search(query, 1000);
		for (int k = 0; k < tops.scoreDocs.length; k++) {
			JSONObject searchjson = new JSONObject();
			Document doc = searcher.doc(tops.scoreDocs[k].doc);
			// 内容增加高亮显示
			// TokenStream tokenStream = analyzer.tokenStream("qaQuestion", new
			// StringReader(doc.get("qaQuestion")));
			// String qaQuestion = highlighter.getBestFragment(tokenStream,
			// doc.get("qaQuestion"));
			/*if (CMyString.isEmpty(doc.get("qaAnswer")))
				continue;

			boolean flag = false;
			for (int i = 0; i < list.size(); i++) {
				String question = doc.get("qaQuestion");
				if (question.indexOf(list.get(i)) == -1) {
					flag = true;
					break;
				}
			}
			if (flag)
				continue;*/
			searchjson.put("title", doc.get("qaQuestion"));
			searchjson.put("id", doc.get("id"));
			searchjson.put("content", doc.get("qaAnswer"));
			jsonList.add(searchjson);
		}
		return jsonList;
	}

	/**
	 * 配置jcseg分词*
	 */
	private static IndexWriter getIndexWriter() throws Exception {
		Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);

		JcsegAnalyzer5X jcseg = (JcsegAnalyzer5X) analyzer;
		JcsegTaskConfig config = jcseg.getTaskConfig(); // 追加同义词到分词结果中, 需要在jcseg.properties中配置jcseg.loadsyn=1
		config.setAppendCJKSyn(true); // 追加拼音到分词结果中,需要在jcseg.properties中配置jcseg.loadpinyin=1
		config.setAppendCJKPinyin(true);
		config.setClearStopwords(true); // 设置过滤停止词 config.setAppendCJKSyn(true);
		// 设置关闭同义词追加 //config.setKeepUnregWords(false);
		Map<String, String> map = PropertiesUtil.getProperties_3("/fileUrl.properties");
		Directory dire = FSDirectory.open(Paths.get(map.get("value")));

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(dire, iwc);
		return indexWriter;
	}

	private static void addSingleDoc(IndexWriter iw, Map<String, String> map) throws Exception {
		Document doc = new Document();
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			doc.add(new TextField(entry.getKey(), entry.getValue(), Store.YES));
		}
		iw.addDocument(doc);
		iw.commit();
	}

	private static void addDocSQL(IndexWriter iw, String sql, Map<String, String> map) throws Exception {
		PreparedStatement ps = null;// 声明
		ResultSet resultSet = null;// 记录集
		try {
			ps = DBUtil.getConnection().prepareStatement(sql);
			resultSet = ps.executeQuery();
			while (resultSet.next()) {
				addSingleDoc(iw, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(resultSet);
			DBUtil.close(ps);
			DBUtil.close(DBUtil.getConnection());
		}

	}

	/****
	 * 删除索引
	 * 
	 * @param key
	 *            文档ID名称"id"
	 * @param value
	 *            文档id值
	 */
	public static void deleteDoc(String key, String value) {
		Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
		IndexWriterConfig icw = new IndexWriterConfig(analyzer);
		// Path indexPath = Paths.get("indexdir");
		Map<String, String> map = PropertiesUtil.getProperties_3("/fileUrl.properties");
		Directory directory;
		try {
			directory = FSDirectory.open(Paths.get(map.get("value")));
			IndexWriter indexWriter = new IndexWriter(directory, icw);
			indexWriter.deleteDocuments(new Term(key, value));
			indexWriter.commit();
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 更新索引
	 * 
	 * @param Map<String,
	 *            String> 键 id\qaQuestion\qaAnswer\qaKeywords
	 * @param key
	 *            文档id名称 "id"
	 * @param value
	 *            文档ID值
	 * @throws Exception
	 */
	public static void update(Map<String, String> strmap, String key, String value) throws Exception {
		Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
		IndexWriterConfig icw = new IndexWriterConfig(analyzer);
		Map<String, String> map = PropertiesUtil.getProperties_3("/fileUrl.properties");
		Directory directory = FSDirectory.open(Paths.get(map.get("value")));
		IndexWriter indexWriter = new IndexWriter(directory, icw);
		Iterator<Entry<String, String>> it = strmap.entrySet().iterator();
		Document doc = new Document();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			doc.add(new TextField(entry.getKey(), entry.getValue(), Store.YES));
		}
		indexWriter.updateDocument(new Term(key, value), doc);
		indexWriter.close();
		System.out.println("更新index完成!");
	}

	/***
	 * 分词
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static List<String> getStrinsByWords(String words) {
		Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
		JcsegAnalyzer5X jcseg = (JcsegAnalyzer5X) analyzer;
		JcsegTaskConfig config = jcseg.getTaskConfig();
		// 追加同义词到分词结果中, 需要在jcseg.properties中配置jcseg.loadsyn=1
		config.setAppendCJKSyn(true);
		// 追加拼音到分词结果中, 需要在jcseg.properties中配置jcseg.loadpinyin=1
		config.setAppendCJKPinyin(false);
		// config.setClearStopwords(true); //设置过滤停止词
		config.setAppendCJKSyn(true); // 设置关闭同义词追加
		// 更多配置, 请查看com.webssky.jcseg.core.JcsegTaskConfig类
		// String words = "如何办理放射源转让审批？";
		TokenStream stream = null;
		List<String> list = new ArrayList<String>();
		try {
			stream = analyzer.tokenStream("myfield", words);
			stream.reset();
			CharTermAttribute offsetAtt = stream.addAttribute(CharTermAttribute.class);
			while (stream.incrementToken()) {
				String string = offsetAtt.toString();
				String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
				Pattern p = Pattern.compile(regEx); 
				Matcher m = p.matcher(string);
				string  = m.replaceAll("").trim();
				if (string.length() > 0)
					list.add(string);
			}
			stream.end();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static String search(String word, int numSug) {
		Directory directory = new RAMDirectory();
		String string = "";
		Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
		try {
			IndexWriterConfig config1 = new IndexWriterConfig(analyzer);
			
			//初始化字典目录
			//最后一个fullMerge参数表示拼写检查索引是否需要全部合并
			//String dirpath = PropertiesUtil.getProperties_5("/fileUrl.properties","value");
			//Directory dire = FSDirectory.open(Paths.get(dirpath));
			SpellChecker spellchecker = new SpellChecker(directory);
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			//IndexWriter indexWriter = new IndexWriter(dire, iwc);
			//spellchecker.indexDictionary((Dictionary) dire, iwc, true);
			//spellchecker.i
			String dispath = PropertiesUtil.getProperties_5("/fileUrl.properties","dicpath");
			spellchecker.indexDictionary(new PlainTextDictionary(Paths.get(dispath)),config1,true);
			//这里的参数numSug表示返回的建议个数
			String[] suggests = spellchecker.suggestSimilar(word, numSug);
			if (suggests != null && suggests.length > 0) {
				for (String suggest : suggests) {
					string = suggest ;
					//System.out.println("您是不是想要找：" + suggest);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}

	public static void main(String[] args) throws Exception {
		String words = "还保厅的电话";
		String string = search(words,1);
		System.out.println("------------" + string);
		
		// LuceneUtil.search("中华人民共和国", new String[] {"qaKeywords","qaQuestion"});
		/*Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
		JcsegAnalyzer5X jcseg = (JcsegAnalyzer5X) analyzer;
		JcsegTaskConfig config = jcseg.getTaskConfig();
		// 追加同义词到分词结果中, 需要在jcseg.properties中配置jcseg.loadsyn=1
		config.setAppendCJKSyn(false);
		// 追加拼音到分词结果中, 需要在jcseg.properties中配置jcseg.loadpinyin=1
		config.setAppendCJKPinyin(true);
		// config.setClearStopwords(true); //设置过滤停止词
		// 更多配置, 请查看com.webssky.jcseg.core.JcsegTaskConfig类
		//String words = "环境保护厅解读人事部？";
		String words = "办工室";
		TokenStream stream = null;
		List<String> list = new ArrayList<String>();
		try {
			stream = analyzer.tokenStream("myfield", words);
			stream.reset();
			CharTermAttribute offsetAtt = stream.addAttribute(CharTermAttribute.class);
			while (stream.incrementToken()) {
				String string = offsetAtt.toString();
				System.out.println(string);
				String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
				Pattern p = Pattern.compile(regEx); 
				Matcher m = p.matcher(string);
				string  = m.replaceAll("").trim();
				if (string.length() > 0)
					list.add(string);
			}
			stream.end();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] strings = {"qaQuestion","qaKeywords"};
		JSONArray array =  search(words, strings);
		System.out.println(array.toString());*/

	}

}
