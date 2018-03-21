import axios from "axios";


const instance = axios.create({
    baseURL: "http://api.wanpeng123.cn/"
});

export default instance;