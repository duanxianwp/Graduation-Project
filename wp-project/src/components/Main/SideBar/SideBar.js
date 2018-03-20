import React, {Component} from "react";
import {ListGroup, ListGroupItem} from "react-bootstrap";
import classes from "./SideBar.css";

class SideBar extends Component {
    constructor() {
        super();
        this.state = {
            flag: false,
            flag1: false,
            flag2: false,
            flag3: false
        }
    }

    eventHandler = () => {
        let flag = !this.state.flag;
        this.setState({
            flag: flag
        })
    };

    eventHandler1 = () => {
        let flag1 = !this.state.flag1;
        this.setState({
            flag1: flag1
        })
    };

    eventHandler2 = () => {
        let flag2 = !this.state.flag2;
        this.setState({
            flag2: flag2
        })
    };

    eventHandler3 = () => {
        let flag3 = !this.state.flag3;
        this.setState({
            flag3: flag3
        })
    };



    render() {
        return (
            <div className={classes.SideBar}>
                <div className={classes.one}>
                    <div className={classes.first} onClick={this.eventHandler}>用户管理</div>
                            <ListGroup className={this.state.flag? classes.itemsShow: classes.itemsDisappear}>
                                <ListGroupItem className={classes.ListGroupItem} href="#">新增用户</ListGroupItem>
                                <ListGroupItem className={classes.ListGroupItem} href="#">更新用户</ListGroupItem>
                                <ListGroupItem className={classes.ListGroupItem} href="#">封禁用户</ListGroupItem>
                            </ListGroup>
                </div>
                <div className={classes.one}>
                    <div className={classes.first} onClick={this.eventHandler1}>奖项管理</div>

                            <ListGroup className={ this.state.flag1? classes.itemsShow: classes.itemsDisappear}>
                                <ListGroupItem className={classes.ListGroupItem} href="#">奖项列表</ListGroupItem>
                                <ListGroupItem className={classes.ListGroupItem} href="#">新增奖项</ListGroupItem>
                                <ListGroupItem className={classes.ListGroupItem} href="#">更新奖项</ListGroupItem>
                            </ListGroup>


                </div>
                <div className={classes.one}>
                    <div className={classes.first} onClick={this.eventHandler2}>工单管理</div>

                    <ListGroup className={ this.state.flag2? classes.itemsShow: classes.itemsDisappear}>
                        <ListGroupItem className={classes.ListGroupItem} href="#">工单列表</ListGroupItem>
                        <ListGroupItem className={classes.ListGroupItem} href="#">待审核</ListGroupItem>
                    </ListGroup>


                </div>
                <div className={classes.one}>
                    <div className={classes.first} onClick={this.eventHandler3}>学分管理</div>

                    <ListGroup className={ this.state.flag3? classes.itemsShow: classes.itemsDisappear}>
                        <ListGroupItem className={classes.ListGroupItem} href="#">逆序列表</ListGroupItem>
                        <ListGroupItem className={classes.ListGroupItem} href="#">按班级查询</ListGroupItem>
                        <ListGroupItem className={classes.ListGroupItem} href="#">按学号查询</ListGroupItem>
                    </ListGroup>


                </div>
            </div>
        )
    }
}

export default SideBar;