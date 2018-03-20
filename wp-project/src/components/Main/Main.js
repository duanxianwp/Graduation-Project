import React, {Component} from "react";
import SideBar from "./SideBar/SideBar";
import classes from "./Main.css";
import Contents from "./Contents/Contents";

class Main extends Component {
    render() {
        return (
            <div className={classes.Main}>
                <div className={classes.upBar}>
                    <h2>XXX管理系统</h2>
                    <div>name</div>
                </div>
                <SideBar/>
                <Contents/>
            </div>
        )
    }
}

export default Main;