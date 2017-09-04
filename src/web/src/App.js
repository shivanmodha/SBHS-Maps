import React, { Component } from 'react';
import { Button } from "reactstrap";
import "./bootstrap-4.0.0/dist/css/bootstrap.css"

class App extends Component
{
    render()
    {
        return (
            <div className="App">
                <Button color="danger">Danger!</Button>
            </div>
        );
    }
}
export default App;
