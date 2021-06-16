package fatcats.top.demo.service;

public interface UserService {
        /**
         * 植入和修改信息
         * @param username
         * @param token
         * @return
         */
        public void jugeInfo(String username,String token);

        /**
         * 根据用户名获取token值
         * @param username 用户名
         * @return 返回token值
         */
        public String getToken(String username);
}
