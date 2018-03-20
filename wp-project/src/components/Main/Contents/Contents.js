import React, {Component} from "react";
import {Breadcrumb} from "react-bootstrap";
import classes from "./Contents.css";

class Contents extends Component {
    render() {
        return (
            <div >
                <Breadcrumb className={classes.Contents}>
                    <Breadcrumb.Item href="#">Home</Breadcrumb.Item>
                    <Breadcrumb.Item href="http://getbootstrap.com/components/#breadcrumbs">
                        Library
                    </Breadcrumb.Item>
                    <Breadcrumb.Item active>Data</Breadcrumb.Item>
                </Breadcrumb>
            </div>
        )
    }
}

export default Contents;