package com.ISquared.linkcleaner;

public final class Constants {
    public static final String FILTERS = "Split,PARM1=,PARM1=,1\n" +
            "Split,murl=,murl=,1\n" + "Split,clicks.slickdeals.net/i.php?u1=http,u2=,1\n" +
            "Split,?gclid,&url=,0\n" + "Split,link=,link=,1\n" + "Split,u=,u=,1\n" +
            "Split,h=,h=,0\n" + "Split,utm_,utm_,0\n" + "Split,&nm_,&nm_,0\n" +
            "Split,ref=,ref=,0\n" + "Split,u1=,u1=,0\n" + "Split,mpre=,ref=,0\n" +
            "Split,&a=,&a=,0\n" + "Split,q=,q=,1\n" + "Split,token=,token=,0\n" +
            "Split,&sa=D&,&sa=D&,0\n" + "Split,ved=,ved=,0\n" +
            "Split,html_redirect,&html_redirect,0\n" + "Split,&v=,&v=,0\n" + "Split,&mpre=,&mpre=,1\n" +
            "Split,&event=,&event=,0\n" + "Split,&redir_,&redir,0\n" +
            "Replace,amp/,amp/s/,amp/s/https://\n" + "Split,amp/s,amp/s/,1\n" + "Replace,amp/,amp/,\n" + "Split,bhphotovideo.com,.html,0\n" +
            "Append,bhphotovideo.com,.html/\n" + "Replace,1225267-11965372?,url=,url=https://staples.com\n" +
            "Split,url=,url=,1\n" + "Split,src=,src=,0\n" + "Split,&source=,&source=,0\n";//"Prepend,1225267-11965372?,https://staples.com\n";
    public static final String THEME = "Theme";
    public static final String ACTIVITY = "activity";
    public static final String INTRO = "IntroTutSeen";
    public static final String FILTER = "filters";
    public static final String DARKV = "Dark";
    public static final String AMOLEDV = "Amoled Black";
    public static final String LIGHTV = "Light";
    public static final String SYSTEMV = "Follow System";
    public static final int DARK = R.string.Dark;
    public static final int AMOLED = R.string.Amoled;
    public static final int LIGHT = R.string.Light;
    public static final int SYSTEM = R.string.System;
    public static final int THEME_SETTING = R.string.theme_summary;

}
