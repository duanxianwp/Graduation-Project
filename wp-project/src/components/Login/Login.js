import React, {Component} from "react";
import {Form, FormGroup, FormControl, ControlLabel, Button} from "react-bootstrap";
import classes from "./Login.css";
import {Link, withRouter} from "react-router-dom";
import axios from "../../axios-requests";
// import axios from "axios";

class Login extends Component {
    constructor() {
        super();
        this.state = {
            account: "",
            password: "",
        }
    }

    submitHandler = (event) => {
        // event.preventDefault();
        console.log(this.state);
        const orders = {...this.state};
        console.log(orders);
        axios.post("/account/login", orders)
            .then(response => console.log(response))
            .catch(error => console.log(error));
        // this.props.history.replace("/main");

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