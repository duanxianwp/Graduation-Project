import axios from "axios";


const instance = axios.create({
    baseURL: "http://api.wanpeng123.cn/"
});

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

export default instance;