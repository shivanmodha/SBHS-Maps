import React, { Component } from 'react';
import './Navigation.css';
import App from './App';

class Navigation extends Component
{
    render()
    {
        return (
            <div>
                <div className="mdl-layout mdl-js-layout mdl-layout--fixed-header">
                    <header className="mdl-layout__header">
                        <div className="mdl-layout__header-row">
                            <div className="mdl-textfield mdl-js-textfield">
                                <input className="mdl-textfield__input" type="text" id="sample1" />
                            </div>
                        </div>
                    </header>
                    <div className="mdl-layout__drawer mdl-color-text--black">
                        <span className="mdl-layout-title">SBHS Maps</span>
                        <nav className="mdl-navigation">
                        </nav>
                    </div>
                    <main className="mdl-layout__content">
                        <div className="page-content"><App at={this.props.at}/></div>
                    </main>
                </div>
            </div>    
        );
    }
}

export default Navigation