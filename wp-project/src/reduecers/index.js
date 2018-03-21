import {LOGIN} from "../actions/index";

const initialState = {
    account: "",
    totalPrice: ""
};

const information = (state = initialState, action) => {
    switch (action.type){
        case LOGIN:
            return {
                ...state,
            }
    }
};