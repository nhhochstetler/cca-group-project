import React, { Component } from "react";
import axios from "axios";
import "./App.css";
import PlacesWithStandaloneSearchBox from "./SearchBox";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      start: { lat: "", long: "", name: "" },
      dest: { lat: "", long: "", name: "" },
      prices: []
    };
  }

  setPlace = (place, value) => {
    this.setState({
      [place]: value
    });
  };

  getPrice = async () => {
    // TODO: may need to update API endpoint
    try {
      const response = await axios.get("/prices");
      const { uber, lyft, taxi } = response;
      this.setState({
        prices: [uber, lyft, taxi]
      });
    } catch (err) {
      console.error(err);
      this.setState({
        prices: [
          Math.floor(Math.random() * 100),
          Math.floor(Math.random() * 100),
          Math.floor(Math.random() * 100)
        ]
      });
    }
  };

  render() {
    const { prices } = this.state;
    const showResult = prices.length > 0;
    return (
      <div className="App">
        <h1> Price comparison between Uber, Lyft and Taxi.</h1>
        <div className="flex-row">
          <div>
            <div className="location-field">
              <span className="location-label">Start Location:</span>
              <PlacesWithStandaloneSearchBox
                placeholder="Start Location"
                onSetPlace={this.setPlace.bind(null, "start")}
              />
            </div>
            <div className="location-field">
              <span className="location-label">Destination:</span>
              <PlacesWithStandaloneSearchBox
                placeholder="Destination"
                onSetPlace={this.setPlace.bind(null, "dest")}
              />
            </div>
          </div>
          <div>
            <button className="go-btn" onClick={this.getPrice}>
              Get Price
            </button>
          </div>
        </div>

        {showResult && (
          <div className="result">
            <h3>Result:</h3>
            <div className="row">
              <div className="result-label">Taxi</div>
              <div>${prices[0]}</div>
            </div>
            <div className="row">
              <div className="result-label">Uber</div>
              <div>${prices[1]}</div>
            </div>
            <div className="row">
              <div className="result-label">Lyft</div>
              <div>${prices[2]}</div>
            </div>
          </div>
        )}
      </div>
    );
  }
}

export default App;
