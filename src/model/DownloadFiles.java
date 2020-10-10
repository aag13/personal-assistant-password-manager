package model;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author aag-pc
 */
public class DownloadFiles {

    private String[] supportedExt = {".pdf", ".zip", ".mp3", ".ppt", ".txt"};
    private MessageDispatcher messageDispatcher;

    public void download(String url, String directory, LinkedList ll) {

//        System.out.println("url : " + url);
//        System.out.println("directory : " + directory);
//        for (int i = 0; i < ll.size(); i++) {
//            System.out.println(ll.get(i));
//        }
        try {
            long start = System.currentTimeMillis();

            //this is to handle if the url given is itself a file not normal page containing files
            if (isBaseURLDownloadable(url)) {
                String fileName = getFileName(url);
                String completeFileName = Paths.get(directory, fileName).toString();
                downloadUsingFiles(url, completeFileName);
            } else {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a");
                for (Element el : elements) {
                    String link = el.attr("href");
                    if (isDownloadable(link, ll)) {
                        String fileName = getFileName(link);
                        String strToDownload = formatUrl(url, link);
                        String completeFileName = Paths.get(directory, fileName).toString();
                        downloadUsingFiles(strToDownload, completeFileName);
                    }

                }
            }

            long end = System.currentTimeMillis();
            long duration = (end - start) / 1000;
            String tempstring = "";
            if (duration >= 60) {
                tempstring = "  Time Taken : " + (duration / 60) + " minute(s)";
            } else {
                tempstring = "  Time Taken : " + (duration) + " seconds";
            }
            messageDispatcher.notifyMessage("Download Complete!!!   " + tempstring);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    String formatUrl(String url, String link) throws Exception {

        String downloadableLink = link.trim();

        if (!downloadableLink.toLowerCase().contains("http") 
                && !downloadableLink.toLowerCase().contains("www")) {
            // handle the end / thoroughly
            // handle valid 404 urls
            // means the given link is relative. generate full downloadable link using the url
            //first check whether  / at the end of the url
//            if (url.endsWith("/")) {
//                downloadableLink = new URL(new URL(url), link).toString();
//            } else {
//                // add / at the end
//                downloadableLink = new URL(new URL(url + "/"), link).toString();
//            }

            downloadableLink = new URL(new URL(url), link).toString();
        }

        return downloadableLink;
    }

    void downloadUsingFiles(String urlString, String fileName) throws Exception {

        URL url = new URL(urlString);
        InputStream is = url.openStream();

        /**
         * to get file size, use either one
         * url.openConnection().getContentLengthLong()/Math.pow(10, 6) + " MB"
         * Long.parseLong(ucon.getHeaderField("content-length"))
         */
        Path filePath = Paths.get(fileName);
        if (Files.exists(filePath)) {
            //file already exists, so dont copy
            messageDispatcher.notifyMessage(filePath + "    =>    already exists");
        } else {
            messageDispatcher.notifyMessage("Downloading    =>    " + urlString);
            Files.copy(is, filePath);
            messageDispatcher.notifyMessage("DONE");
        }

        is.close();
    }

    boolean isDownloadable(String link, LinkedList allowedExt) {

        for (int i = 0; i < allowedExt.size(); i++) {
            if (link.endsWith((String) allowedExt.get(i))) {
                return true;
            }
        }
        return false;
    }

    boolean isBaseURLDownloadable(String url) {
        for (int i = 0; i < supportedExt.length; i++) {
            if (url.endsWith(supportedExt[i])) {
                return true;
            }
        }
        return false;
    }

    String getFileName(String link) {
        int last = link.lastIndexOf("/");
        String name = link.substring(last + 1);
        return name;
    }

    public void setMessageDispatcher(MessageDispatcher md) {
        this.messageDispatcher = md;
    }

}
