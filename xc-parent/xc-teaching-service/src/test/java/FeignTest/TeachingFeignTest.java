//package FeignTest;
//
//import com.xuecheng.agent.teaching.CommentApiAgent;
//import com.xuecheng.api.comment.model.CommentDTO;
//import com.xuecheng.common.domain.page.PageVO;
//import com.xuecheng.common.domain.response.RestResponse;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class TeachingFeignTest {
//    @Autowired
//    private CommentApiAgent commentApiAgent;
//    @Test
//    public void test1(){
//        RestResponse<PageVO<CommentDTO>> response = commentApiAgent.list(params, model);
//        System.out.println(response);
//    }
//}
