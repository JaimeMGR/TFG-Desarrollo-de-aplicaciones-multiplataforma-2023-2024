package com.JaimeMGR.maxmangacommunity.Notificaciones;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


    @Headers({
            "Content-Type:application/json",
            "Authorization: Bearer ya29.c.c0AY_VpZg0igAfkSPcDU146OT-B7UDUw518Ve9vyGYPAzSYf20OPxw5oX37P_OtEXQjPDMdLuVMM5L4SiwiTmb9HjnZi0aVguiRlZn78-lHf7gKkY_dcn9_06hRTUbyjfKapNeMVe95hzopAVz1ApJuSsBJRJzf1_zfD0TQ1WBC1xDiesQBxnOtYaG2e4Who0gHUgrIzAibtzkVFA8cSqsqDnx9B-t1b96HoJ2Qv8a8OJcrBD4dOzxHqx_m0F1D_0gH-bk1xqECV-8aJsWX-Qsj6puTIWxletBohEr2Em-mKi9CPoqEfYrZ4QAUQYVsjbsgglquARwJqXTQUL7RHOIOfWpIG76JaFggRtYnElPFZaK3USG-NyWN0gML385DcggplnwZvhJm3y6d9-J18u9zVxp_jYSQ4jUVWXy4FWY-m3M7ckpyJOzaF4vaQigBq7YzYsm9ikMb5nQwt8z4Owpxltki2cSddZqwrsVv2bkp2vQXqkZIulhwYdVoMtkfsbc5fM3tjBhW3cV06tBuUZV90hxapjnZIXjgFSM0Q1ZRB3qBl2JcdptMrskfIbQh7syh-rQ5bgxa44JwXaeQgp4t_BsVkxUa8wqjo4frXl1Oz71eUVOq2trklFdSalbu3JzhMtXx8vJ51saZ2gB1kwbYMIIvhB6Yf4amFRpYpVOnQFQp_dJpf9Il378c7Om97knfRfqs8xsQ4qnMkh1p--61viennn9ovrMVu78doWMwdiJ9u5Xxjs2oRkxIbceQowJpnm12aQskSXS8jh6MVM3-WUY_YiWlFF42WupwyOY1Z8-2bp_zQRj17iO-_RWrV2oeS5yd_Oj92_7Bqsw2cg4FMylu6i6ZfrwxSjtrzXqpe74vXuYyl6khzF-bVfmbSwFkeJ0429YQ1qY5B7jR-4UcxmQXv2FbW6khifnkpzWgfxgsfo0bVn5z8jp3nxUQUbfct_V8UZ9hvx3RWc2gWw9UvZ0i0589RvZJ45eWx6vdi-fap4Yyof_12z"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
