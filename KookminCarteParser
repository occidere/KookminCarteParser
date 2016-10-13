package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Calendar;

public class KookminCarteParserJsoup {
	private static final String address = "http://kmucoop.kookmin.ac.kr/restaurant/restaurant.php?w=";
	private static int i;
	
	public static void main(String[] args) throws Exception {
		String today = findToday();
		bubsikPrint(today);
		System.out.println();
		haksikPrint(today);
		System.out.print("\n종료하려면 아무키나 누르세요...");
		System.in.read();
	}
	
	private static String findToday(){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH)+1, date = cal.get(Calendar.DATE);
		return year+"년 "+month+"월 "+date+"일";
	}
	private static void bubsikPrint(String today) throws Exception {
		System.out.println("## 법학관 한울식당 메뉴 ("+today+") ##\n");
		String tmp; i = 0;
		String bubsikMenu[] = { "바로바로1", "바로바로2", "면이랑", "밥이랑 하나", "밥이랑 두울", "石火랑", "石火랑(조식)"};

		Document doc = Jsoup.connect(address+1).get();
		Elements menu = doc.select("td[bgcolor=#eaffd9]");
		
		for (Element res : menu) {
			tmp = res.text();
			if (tmp.contains("*중식")) tmp = tmp.replace("*석식*", "\n*석식*");
			bubsikMenu[i] = "------- <"+bubsikMenu[i]+"> -------\n" + tmp+"\n";
			
			if(1<i && i<6) System.out.println(bubsikMenu[i]);
			i++;
		}
	}
	private static void haksikPrint(String today) throws Exception {
		System.out.println("## 복지관 학생식당 메뉴 ("+today+") ##\n");
		String haksikMenu[] = { "착한아침", "가마", "누들송(면)", "누들송(카페테리아)", "인터쉐프", "데일리밥"};
		
		for(i=0;i<6;i++) haksikMenu[i] = "------- <"+haksikMenu[i]+"> -------\n"; i=0;
		
		Document doc = Jsoup.connect(address+2).get();
		Elements menu = doc.select("td[bgcolor=#eaffd9]");
		
		for (Element res : menu) {
			String tmp = res.text();
			
			if(i==0) tmp = "*조식*"+tmp+"\n";
			else if(i<6) tmp = "*중식* "+tmp+"\n";
			else tmp = "*석식* "+tmp+"\n";
			
			if(i==6) haksikMenu[1]+=tmp;
			else if(i==7) haksikMenu[4]+=tmp;
			else if(i==8){ haksikMenu[5]+=tmp; break; }
			else haksikMenu[i]+=tmp;
			i++;
		}
		
		for(i=0;i<6;i++) System.out.println(haksikMenu[i]);
	}
}
