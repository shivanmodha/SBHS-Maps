import React, { Component } from 'react';
class HomePage extends Component
{
    render()
    {
        return (
            <div style={{
                padding: 10
            }}>
                <h3>Welcome to SBHS Maps</h3>
                <p style={{ textAlign: "left" }}><i>Maps for school</i></p>
                <h3>Getting Started</h3>
                <div style={{ maxHeight: 200, overflowX: "hidden" }}>
                    <h1 className="display-3" style={{ fontSize: 20 }}>Explore</h1>
                    <p style={{ textAlign: "justify" }}>
                        Looking for a classroom? Curious about a mysterious room? Search for anything! Get directions to anywhere!
                    </p>
                    <h1 className="display-3" style={{ fontSize: 20 }}>Quick Tutorial</h1>
                    <p style={{ textAlign: "justify" }}>
                        Use the <strong>mouse</strong> to navigate the map and the <strong>mouse wheel</strong> to zoom. The buttons on the <strong>bottom right corner</strong> allow for additional map navigation as well as floor control. You can also find these options by <strong>right clicking</strong> anywhere on the map. 
                    </p>
                    <p style={{ textAlign: "justify" }}>
                        Click on a room to display additional information and other special details. If you can't find a room, try <strong>searching</strong> for it. You can also get directions to a room by expanding the direction dialog by cliking the <strong>location</strong> button next on the searchbar, and entering your desired destination. Once you click on the <strong>left arrow</strong> button, you will get step by step directions. Clicking on any of the directions listed will center that location.
                    </p>
                </div>    
                <h3>Contact</h3>
                <p>Be sure to check out SBHS Maps on GitHub and submit any bugs you encounter or any feature requests you would like.</p>
            </div>
        );
    }
}
export default HomePage;