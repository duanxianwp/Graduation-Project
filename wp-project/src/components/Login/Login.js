import React, {Component} from "react";
import {Form, FormGroup, FormControl, ControlLabel, Button} from "react-bootstrap";
import classes from "./Login.css";
import {Link, withRouter} from "react-router-dom";

import axios from "../../axios-requests";


class Login extends Component {
    constructor() {
        super();
        this.state = {
            account: "",
            password: "",
            success: false
        }
    }

    submitHandler = (event) => {
        // event.preventDefault();
        // console.log(this.state);
        const orders = {...this.state};
        // console.log(orders);
        // axios.post("/account/login", orders)
        //     .then(response => console.log(response))
        //     .catch(error => console.log(error));
        // this.props.history.replace("/main");

        let params = new URLSearchParams();
        params.append("account",orders.account);
        params.append("password",orders.password);

        axios({
            method: 'post',
            url: '/account/login',
            data: params
        }).then((response) => {
            let msg = response.data.msg;
            console.log(msg);
            msg === "成功" ?
                this.props.history.replace("/main")
                :
                alert("账号或密码错误");
        });



        // $.ajax({
        //     url: "http://api.wanpeng123.cn/account/login",
        //     type: "POST",
        //     data: orders,
        //     success: function (response) {
        //         console.log(response);
        //     }
        // })

        // fetch('http://api.wanpeng123.cn/account/login', {
        //     method: 'POST',
        //     // mode: 'cors',
        //     // credentials: 'include',
        //     headers: {
        //         'Content-Type': 'application/json;charset=UTF-8'
        //     },
        //     body: orders
        // }).then(function(response) {
        //     console.log(response);
        // });

    //     fetch('http://api.wanpeng123.cn/account/login',
    //     {
    //         method:'POST',
    //         data: orders
    //     }
    // )
    // .then(res => res.json())
    //         .then(json => console.log(json))

    };

    render () {


        return (
            <div className={classes.Login}>
                <Form>
                    <FormGroup className={classes.Group}>
                        <ControlLabel className={classes.Label}>账号: </ControlLabel>
                        <FormControl type="text"
                                     placeholder="Please inter your name"
                                     className={classes.Control}
                                     onChange={(event) => this.setState({account: event.target.value})}
                        />
                    </FormGroup>
                    <FormGroup>
                        <ControlLabel>密码: </ControlLabel>
                        <FormControl type="password"
                                     placeholder="Please inter your password"
                                     className={classes.Control}
                                     onChange={(event) => this.setState({password: event.target.value})}
                        />
                    </FormGroup>
                    <Button className={classes.Button}
                            onClick={this.submitHandler}>Submit</Button>
                    <Link to="/findPassword" className={classes.Link}>找回密码</Link>
                </Form>
            </div>
        );
    }
}

export default withRouter(Login);