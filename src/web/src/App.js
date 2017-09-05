import React, { Component } from 'react';
import { Alert, UncontrolledAlert  } from 'reactstrap';
import Navigation from './Navigation';
import MapComponent from './MapComponent';

class App extends Component
{
    render()
    {
        return (
            <div>
                <MapComponent />
                <Navigation />
            </div>    
        );
    }
}
export default App;
