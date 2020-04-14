import React, {Component} from 'react';
import {Field, reduxForm} from 'redux-form';

class NewPostPage extends Component {

    render() {
        return (
            <form>
                <Field name="Title" />
            </form>
        )
    }

}
