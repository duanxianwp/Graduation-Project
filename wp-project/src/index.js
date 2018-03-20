import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import {BrowserRouter, Switch, Route} from "react-router-dom";
import registerServiceWorker from './registerServiceWorker';
import Main from "./components/Main/Main";
import FindPassword from "./components/FindPassword/FindPassword";



ReactDOM.render(
    <BrowserRouter>
        <Switch>
            <Route exact path="/" component={App}/>
            <Route path="/main" component={Main}/>
            <Route path= "/findPassword" component={FindPassword}/>
        </Switch>
    </BrowserRouter>
    , document.getElementById('root'));
registerServiceWorker();
