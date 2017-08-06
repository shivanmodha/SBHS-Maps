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
                            <button id="drawer" className="mdl-layout__drawer-button">
                                <i className="material-icons">menu</i>
                            </button>
                                <div className="mdl-textfield mdl-js-textfield">
                                <input className="mdl-textfield__input" type="text" id="searchbar" onKeyUp={this.Search}></input>
                                    <label className="mdl-textfield__label" htmlFor="searchbar">Search SBHS Maps</label>
                                </div>
                                <button className="mdl-button mdl-js-button mdl-button--icon" onClick={this.Search_Click}>
                                    <i className="material-icons">search</i>
                                </button>
                            <button className="mdl-button mdl-js-button mdl-button--icon">
                                <i className="material-icons">directions</i>
                            </button>
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
    Search_Click(event)
    {
        window.location.href = "@" + document.getElementById("searchbar").innerText;
    }
    Search(event)
    {
        if (event.keyCode === 13)
        {
            window.location.href = "@" + event.target.value;
        }
    }
}

export default Navigation