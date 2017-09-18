import React, { Component } from 'react';
import vs_logo from './images/vs_logo.png'
class AboutPage extends Component
{
    render()
    {
        return (
            <div style={{
                padding: 10,
                maxHeight: 400,
                overflowX: "hidden"
            }}>
                <h3>About SBHS Maps</h3>
                <h1 className="display-3" style={{ fontSize: 20 }}>General</h1>
                <div style={{ width: 325, height: 150, marginTop: -45, backgroundImage: "url(" + vs_logo + ")", backgroundSize: "cover" }} />
                <p style={{ textAlign: "right" }}>
                    <i>(van|sh) studios</i>
                </p>
                <p style={{ textAlign: "justify" }}>
                    Developed by Shivan Modha, <i>Vanish Studios</i>.
                </p>
                <p style={{ textAlign: "justify" }}>
                    Version: 2.1.1 <small>(build 2017-09-17)</small>
                </p>
                <h1 className="display-3" style={{ fontSize: 20 }}>Binaries</h1>
                <p style={{ textAlign: "justify" }}>
                    SBHS Maps is modeled after <i><a href="http://www.sbschools.org/schools/sbhs/">South Brunswick High School</a></i>.
                </p>
                <p style={{ textAlign: "justify" }}>                
                    Graph Version: 2.3.1
                </p>
                <p style={{ textAlign: "justify" }}>
                    Achieved with <a href="https://shivanmodha.github.io/bench/">Graph Workbench</a> <small>(1.0.5-unreleased)</small>
                </p>
                <h3>Misc</h3>
                <h1 className="display-3" style={{ fontSize: 20 }}>Support</h1>
                <ul>
                    <li>Web</li>
                    <li>Desktop</li>
                    <li>Android</li>
                </ul>
                <h1 className="display-3" style={{ fontSize: 20 }}>Versions</h1>
                <ul>
                    <li>Web
                        <ul>
                            <li>2.1.1</li>
                            <li><a href="https://shivanmodha.github.io/maps/sbhs/v1/component.html?floor=0&lox=50&loy=50&loz=60&rox=0&roy=0&roz=0">2.0.0</a></li>
                        </ul>
                    </li>
                    <li>Android
                        <ul>
                            <li>1.0.8</li>
                        </ul>
                    </li>
                </ul>
            </div>
        );
    }
}
export default AboutPage;