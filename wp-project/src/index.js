import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import {BrowserRouter, Switch, Route, HashRouter} from "react-router-dom";
import registerServiceWorker from './registerServiceWorker';
import Main from "./components/Main/Main";
import FindPassword from "./coxxmponents/FindPassword/FindPassword";

ReactDOM.render(
    <HashRouter>
        <Switch>
            <Route exact path="/" component={App}/>
            <Route exact path="/main" component={Main}/>
            <Route path="/findPassword" component={FindPassword}/>
        </Switch>
    </HashRouter>
    , document.getElementById('root'));
registerServiceWorker();
