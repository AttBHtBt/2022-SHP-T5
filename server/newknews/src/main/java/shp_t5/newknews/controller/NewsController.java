package shp_t5.newknews.controller;


import com.opencsv.exceptions.CsvValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import shp_t5.newknews.dto.NewsAllDto;
import shp_t5.newknews.dto.NewsDto;
import shp_t5.newknews.dto.NewsListAllDto;
import shp_t5.newknews.dto.NewsListDto;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.opencsv.CSVReader;
@RestController
@Api(tags = {"News API"})
@CrossOrigin(origins="*", methods={RequestMethod.GET, RequestMethod.POST})
public class NewsController {

     List<NewsDto> listTestNewsDto = new ArrayList<>();
     List<NewsAllDto> listNewsDtoAll = new ArrayList<>();

    // 파일 읽기
    NewsController() throws IOException, CsvValidationException {
        File newscsv = new File("C:\\Users\\82105\\OneDrive\\바탕 화면\\산협프 newknews\\2022-SHP-T5\\server\\newknews\\src\\main\\resources\\csv\\data_221202.csv");
        File newsAll = new File("C:\\Users\\82105\\OneDrive\\바탕 화면\\산협프 newknews\\2022-SHP-T5\\server\\newknews\\src\\main\\resources\\csv\\cluster_article.csv");

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(newscsv),"euc-kr"));
        BufferedReader brAll = new BufferedReader(new InputStreamReader(new FileInputStream(newsAll),"euc-kr"));



        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(newscsv), "UTF-8"));
        CSVReader readerAll = new CSVReader(new InputStreamReader(new FileInputStream(newsAll), "UTF-8"));
        String[] newsline;


        while((newsline = reader.readNext())!=null) {
            String[] newsCut = new String[8];
            int i = 0;
            for (String token : newsline) {
                newsCut[i] = token;
                i++;
            }


            NewsDto testNewsDto = new NewsDto(newsCut[0], newsCut[1], newsCut[2], newsCut[3], newsCut[4], newsCut[5], newsCut[6], newsCut[7]);

            listTestNewsDto.add(testNewsDto);

        }

        while((newsline = readerAll.readNext())!=null) {
            String[] newsCut = new String[6];
            int i = 0;
            for (String token : newsline) {
                newsCut[i] = token;
                i++;
            }


            NewsAllDto testNewsDto = new NewsAllDto(newsCut[0], newsCut[1], newsCut[2], newsCut[3], newsCut[4], newsCut[5]);

            listNewsDtoAll.add(testNewsDto);

        }

    }

    @ApiOperation(value = "뉴스 전송", notes = "뉴스 cs v파일에서 뉴스를 제공하는 API입니다.")
    @GetMapping("/getNews")
    public List<NewsDto> getNews(){

        return listTestNewsDto;
    }

    @ApiOperation(value = "뉴스 키워드로 검색", notes = "뉴스 csv파일내 키워드 글자가 들어있는 헤드라인을 필터링해 제공하는 뉴스 검색API입니다.")
    @GetMapping("/getNewsTitle")
    public NewsListAllDto getNewsTile(String title){
        try {

            List<NewsAllDto> filteredNews = listNewsDtoAll.stream()
                    .filter(n -> n.headline.contains(title))
                    .collect(Collectors.toList());


            filteredNews.stream()
                    .forEach(v -> {
                        try {
                            v.setContent(v.content.substring(0,100)+"...");
                        } catch (Exception e) {
                            try{
                                v.setContent(v.content.substring(0,10)+"...");
                            }catch (Exception newe){
                                System.out.println("substring error");
                            }
                        }
                    });



            NewsListAllDto newsListAllDto = new NewsListAllDto();

            newsListAllDto.setListNews(filteredNews);
            return newsListAllDto;

        }
        catch (Exception e){

            System.out.println(e.getCause());
            return null;
        }

    }

}











