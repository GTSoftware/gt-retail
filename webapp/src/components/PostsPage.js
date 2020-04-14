import React, {Component} from 'react';
import { Link } from 'react-router-dom';

export class PostsPage extends Component {

    render() {
        return (
            <div className="p-grid">
                <div className="p-col-12">
                    <div className="card">
                        <h1>Posts</h1>
                        <Link to='/newPost' >
                            Add new Post
                        </Link>
                        <p>Here we'll be listing new posts</p>

                    </div>
                </div>
            </div>
        );
    }
}