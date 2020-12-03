package com.senyk.volodymyr.tiktoklike.data.datasource

import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.SetLikeResponse
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.UserInfoResponse
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.VideosResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface TikTokApi {

    @GET
    fun getQueryCompletable(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Completable

    @POST
    fun postQueryCompletable(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Completable

    @GET
    fun getUser(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<UserInfoResponse>

    @POST
    fun setLike(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<SetLikeResponse>

    @GET
    fun getVideos(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<VideosResponse>

    @POST
    fun followUserPost(
        @Header("authority") authority: String = "m.tiktok.com",
        @Header("content-length") length: String = "0",
        @Header("htc6j8njvn-b") b: String = "-j4xqw4",
        @Header("htc6j8njvn-d") d: String = "AAaihIjBDKGNgUGASZAQhISy1ELX7IV1St5xCP_____dWkCBAMz7AO2j6P9jeqsfAKXYHmk",
        @Header("user-agent") agent: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36",
        @Header("htc6j8njvn-z") z: String = "q",
        @Header("content-type") type: String = "application/x-www-form-urlencoded",
        @Header("htc6j8njvn-c") c: String = "AADsVCl2AQAAAzdRP_CTjA49kWcPvWUOdCXBNKX-FPt7hkEu-dfshXVK3nEI",
        @Header("accept") accept: String = "application/json, text/plain, */*",
        @Header("htc6j8njvn-a") a: String = "s-waHqVpWob==h_RRY=orYhl2xBfvHGylxD6lK4caYwoXXWTaqAwL2i3xKzfPlxkwV7b09MvEFseY54-=vLuCSMEgc5WoQvwOD76R-fzTvvudV_ZfW6iIwbGkmYmJ_6H=_cfj47dqXZALI-a041EG=PViolN4P3J4=pL6nW7LPQNel2Zk7Racm1lhNrib4UQJZ-JcpKDVhUPcssMjxZL=kzhlIPlaFIRC0LQLn4P2k=4Cq6PQa57q_POrIkIKTU_qPJ_OQEVEdxxtgP6s05oIee_=3ct1rVPZPgRt7RwwXkea9-wJc4QB0QN57Mm2wOjcqIWLWzpPhDQFFc=6zYklzmLnLPC4n__AEFIkZrJ9quxMzKsXTKKe2afJDGyt9buASCz5lOxuxc2LsynadySza_26Y9sRNU45z5RkyxmUWnQ2xZXrYmpX2tqY-ADJIiS=oJqIXozWLFZtlSQ_ye0Xiik_uK69X7mSVDRM__bXmneDIacWPzIBbZAlOav79YMGJWQTu6W65gtJfFig7jW7ULw1oTVn3itB9XfR9DYQKkEh4Fvbs27XU_qL_B6eQzJZFSYjqZ1fN66J7uUW_-rrK6ASihWc9sa55itkxAbsmDSinEI=-D6scl0TtCUwyjIFhPoUnAhwDcOsSi0sTZYxcXCG-Rzdi0Fns4WcBLAjrKcJ0bQT0zX6nhCcxH=IfUf4l2nD2lCnJcX=ZtPkGWdk4-k5VkMeqPGx9c335aDuRhTus54Kb6XK=WaSJLljq2QEeYctPeK0eIwgRhWC=BstN=NWvIOSbls-zU-oAUW3HdWG73XLBjii1qd-HiKn9gcVxOUfNhzrlYbTJGnwFPUrymSs14=gBYm41k2MXEHeJRN4NlfKSlUqUK_Ht9BzIkKHsqdO4lw7UbwBEtbw6k=M9MzGi36VvbX3OGQ0=zI4s_yGJLvSlE=Rk2CHtCXWwzpTTK-uBAkUR9zaudikntMsB3seiKTW_LbNK1ru=wGikiVIYGUJBCddfirVBSp2IRT4g0DV=9FmItGfcruQh_AaG=u-CPzyD=sE35VgsmiiXgbbWS6eb96YzsGALRA=0J6YNA7AnQ4GJ5Y9QNCzmHT3mr4U3pKgWpDUZJsVupgKjAErpEClpmj_5rbnPkMTcbwlJoQH2AJIt0ifeOfwBl=VTOLxNDiMXbTenoXBiBTg0es_BPy10w7RM4WK==LEk5oqF_LaGyaaYs4-6p_pEoS9a5JYDXvm53ff46Bd7aklwOBpIbHZTmzR4DBJLuxJ9QsF2m6ceYpTxlitfhd1K6o3Qb9uY9voBpO7Y-V5rGXxNsgUAAjBkBm-zcvKdxaQyWEEReQxf7O4S0=4AYmD20NYyuGFjR5wYZVyPkWS19Y45dLTtRbK76Gl_h6MDNJzyR0QXsvBKsfhZYf4jjEFLeHLDoigPIUXvqCd4cMku5_ys=eCcMCcT0fgFWGcKA2Zd-ldrQBOedcNMnqTbcdCinwBQu=zqjGGOb57FmYbAQ4lb9MQV_3-qAcp7GEGojrpPoUQlkLM__vp_ttstJss9DzkTr7HDpHBjd5cRUe5WfwZNJgeBazTSdALI-jV75-gWG=BrIB-0holi7ryZPgiyvxSB=hBEQjZvt4shTKY4CEjOR1eobC6JvW46tAyooUFERXNO0bzjP=5lL5P6vmRs4T30JBPnFk0lG7Q-HJvpNaO6GwLwC_ZpNlS=xtvC4IsVCgdpPkGWB9hmFF54Rd0PkKK0q16DKIbBYHJNqvgjxgVznUfqObdIhtjwte3a7K3VqJHlmFcVvD_Yc6kwIhQRlda1Sa-16g1vUgb3rWCfoXSPeSGIDZ1Cmg0H-IWNqYtEjHHgUPVzZyNoBxy=FLdKAKCGf4X3MWO0bsQPcpR06Vr5p3lWWKzjQ5N7shlA6qooJAAq75hknfsYm34ehb7vv-ZhmUEYwU0hJEvu46wf71i=wFXiJz9C2NOsvYfXtZqD50FmlvehES4t0e-7b92c9-dCBG0xV-wsm=kFOvixK1=-vNG0lI1z=vYrNw1TYeV24guGsPslF1=Ytv9N5voUBvf5CbtOmdNoqPxDYIvUGD4Gty-67RNKLzjo4QMO5CS-qPQsohABpR_ECsVOTyJDZX3TdS7PtLdV2IxMJ-KzB7b0DbcTTulQH9EQvMVaMAvQAWzgfHdXZCfAV1DMIdxdTn_NVANjJxTgOWM=JIg2AfsIMHl92Oo_B1JZ3BrfSzFWSG6Awmk9fb09dFffC9d7U9uzWRkUoFCAc3tlHEFKn9W6VTpaHr06nHVTUzd==DrAQceFwgqq99UIHWF7LCKYnl7mtyB9OxigaPhmLdaDHci6NeVOYtxZf_P53aAY=yAgqKaX2cWdflt5_MXQquqk2UnfkJNtL0de4Qc1H5x=wy59DeL-_1vRBCMJ-2Q-jRf5Mh-ValB9QZMm5JDQpgglmbvlnp0p0q0_0rvqvWQ5LiR4VS7b=Faxq9NxCFdGbUyWPoZ4FZSQBbBfyxJ7XSoutLEyW5hdd10=mN2CHzK0hPXd-32LV-zZWB_RSk2Uu5Cq0pT_RM5lVxAEX73jKcWD1BVxIdU-YnGEMbGBEhc5O=-Hw=7QZT5_MzoPMsN1yFu9IQg=j1Ix0DfRFJvhvj=bqrrA1HUw1s0vxQneTA=vTwCZbAeFwspsFCXOXTbxGgZLR66RoEowq_9DjcXBF2ZTf66Di2zywhsZhlLBKnbubcKGCuJZTv91tge6kfdbaExra2J0wovlhFyaT_sWiTCDqVQ_bRJZTFjQkqScSJ3q1nenqRaWTYYr2mV60-m-kLgfXIz6K-q-YZtnTaYQZeIBMWIif4AyD-7CoQ=_g5vvJ4KFiQIuQi7s2DjTlbol2dNgB-2W9K-PBGVgNYTMBUGuwYvzmk9EhcdOX9LXwe1e4MZUWnMWMdJUyyUMkoF4D-QYpsXYFpvsCh2MFJkY6FhMByjp4IipWVkktfOx=49wNu6tIKEEQf1TpG6BzzHcLiyBKe=feER=oH4SO6z7eBzLTA0F5b0ndS-5qT9yEc2D6pNFLUmhuNAOQl=JirkqAKAmYcXhZcfqBtScSvh9HYdreAe6yz4webQcQxPW_qC5k1Xl463ofAc=yQ9GdWI0t0h9SB9t6hVB9Vg=r=FnJqsR3KgdeXwLDAnf6dAHHq7r2-WhpPJSOG__LLK_SZkUoRgHqlI4=iWN2eSRc-nlWfQcPk16SPuUJrY190gLdNQ=stxTaAmlMZwez5Ep=OSbt7ABDraDVWujCE=5zukSrFwmo9fPIbz-vJ59ebqpozEISJYqveC6SKcnJox4mbacl2Mteq1D6MREc73rYWcZhig1yg45PTL1_EEBnojdilxoy6NWz23aqqg9lO5MkyvBxTJy33eyIHpAlOz3H57tJbtnTHH-JU425=7h_O6FsADTtzW4p9skTxKS2oGrIIB9Wnmf3MF=LDWX=WMAUzli1-SHcy5Zi0wut3ei2bw_nIZPktmipl5rGaZkaTGfAziNN6lrfEnGU7xeho616aGi476bDWMmZw5AqIbITq90aU5oOs2QtE=PHAe1hG5vJkOD2udEkaB2OOawwi7egnT6bB7Ji0LVlRskw1B_P9VpHx6tHV2ao1LtG2UrlFX17EI7Xi37annj=Ls3NJJQOxUkjUWPOg1c3sep9CXI_EguOZBbOCCyH7cdBTQz=AOTcnfbQdudrOtN0ljR7VElkATENl-Cqnf9vUi5LMaVlSCZjWq-V37cYuR=n-Z2yFw3N=nFn3PNH-xTOv7hkA7doTtQEZVKd0oLNTSyzIiAKll09IFSZ1UWOuVrHQasiV1uQX6Mq5pwqDgmdrJtr3BRv1t06xTlNMd9L16H5ZPDD=FsKOJraNV2o0RQ5c5FvwMF2xTjyHZ2PZfpoooNmXikvOGONN-N9SLCrdGdjYNAYBwUr_4TWH9IPaLhwTmQB=qKiwxqrxKOsYdau4V0j5pRFvMBtsikvVvLbX_pIyd0ia=1ni7_Su3IPzNiLepnCDXcbYbzsXkPLddQZBxYzWBFIjXSrHJbKGv=5zrjyaAR21il-1eF67_ASUXFgOUIB9BJiVorcnQsRL04gH0dwzFinLT7e70_9_F5gbsUS1xh5izUG=N6pAUST3U-vM5=DncOr0b31NNWRfOUuTujO2rxfb4Xm5z=zbunT0fmxDKi-ZDQJZ7qS=zrr6o9Or0M-TYWD0EvSZ4l5K9COf=xUyZWPbMROOr23xa=Kl-ui=BUbeau=GkGd-Q5fzX-hfut6MPCeYNlOktgOFDN9hUZ5k2l00lJNl5Em7pWK1B==WZGYAQ-eF5eC1JU1xuy-mJVJ-EVWbu=Z=CgmFjaAmQrDdiGSHj7b0VLd5DRFW=itjRPTo0=Hug1rgyPZeDRHghahIgiU2PUEvZq_ggIds0Lvaj05604HIRTTbSpz5LIEepNOE9j-Jjmj51S7QvwmtSE__2fQjLYaSvcBR5cVJfkgAeiQkTQNsKYzCS=epbmIdGRcwP7mQK2VsD6TapoxYPhtKes5EB7WrExLmIQHHf6h3CxMqGspzGJESJ50aFr3CMk1yNPBkkhuz3B9dxvXASI4n1Ep_R1OBxzK7TN9ETs3ub_R4WGWc32nA4sg4TY-sXgB=UQVdcmD13mVxKp6loXDMmGQ3ZEbbSNr-m0_DiMhMsV0zsh=MJfin6BjHBZA0SvEVxIzMFOv1pzQ2THZGlfqJPAzoaUFYNSu=WUhlqp5pfw-xB23oYhOaa7hXznDq06sxeHk2hDxkKBOEB4tQxeWf_UtSpl0LxjxaPrYOJTXw2upBsC9UhVXnumDK1geMPxlRY2lIa6BaMTPM6au6RNU29N=V=srRpKKuQ_HwnlwwbNEkToq9aSRISdHO3f320GrfcAVKp-XlQKC",
        @Header("htc6j8njvn-f") f: String = "A5XSVyl2AQAAdJd3NvPxWX32TUzPdAX-bD3028LtOkAAIWOabvXIwWpqzL1QAVJ163SucgA7wH8AAOfvAAAAAA==",
        @Header("tt-csrf-token") token: String = "lI39-VyQIOX6EU78j9lbDCoO",
        @Header("origin") origin: String = "https://www.tiktok.com",
        @Header("sec-fetch-site") site: String = "same-site",
        @Header("sec-fetch-mode") mode: String = "cors",
        @Header("sec-fetch-dest") dest: String = "empty",
        @Header("referer") referer: String = "https://www.tiktok.com/",
        @Header("accept-language") language: String = "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
        @Header("cookie") cookie: String = "tt_webid_v2=6896044981934900742; tt_webid=6896044981934900742; tt_csrf_token=lI39-VyQIOX6EU78j9lbDCoO; ttwid=1%7C1_m-3Wn6TOsI7VaMu4fR-XHe-t2QDh7NylpNq-KzocU%7C1605610605%7Caa965981529adf24c8e434f1415e4528a99b1ac0d24734d926103af40307c737; passport_csrf_token=452e55a230ff8e37301132f94c73c965; store-idc=alisg; store-country-code=ua; passport_auth_status=f22504a9d0568284c00ee875deb16d40%2C0f12098c02d205a2de0a7b779b0ef054; sid_guard=ba318c4bafe6a9f26922031804d07b31%7C1606215545%7C5184000%7CSat%2C+23-Jan-2021+10%3A59%3A05+GMT; uid_tt=eab67b631aeb8f58f7fc73f796233ecc237e811969428353b41c10bc2bacb6ab; uid_tt_ss=eab67b631aeb8f58f7fc73f796233ecc237e811969428353b41c10bc2bacb6ab; sid_tt=ba318c4bafe6a9f26922031804d07b31; sessionid=ba318c4bafe6a9f26922031804d07b31; sessionid_ss=ba318c4bafe6a9f26922031804d07b31; odin_tt=3a76117a2bb2646fd312df409a2049ca8fe0d2fb3acaad3b5cf75fa8714ea99f1a31baae63c706ccc51c7027293afcecf6f2b8fc992a6377bd346288efd24705",
        @Url url: String
    ): Completable
}

//      This headers are dynamic
// htc6j8njvn-a
// htc6j8njvn-b
// htc6j8njvn-c
// htc6j8njvn-d
// htc6j8njvn-f
