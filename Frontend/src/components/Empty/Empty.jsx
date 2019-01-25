import React, { Component } from 'react';

// Import custom css styles
import './Empty.css';

export default class Empty extends Component {

  constructor(props) {
    super(props);

    this.drawOrbit = this.drawOrbit.bind(this);
    this.drawPlanet = this.drawPlanet.bind(this);
  }

  componentDidMount() {
    // Get canvas context
    let innerCanvas = this.innerCanvas;
    let innerContext = innerCanvas.getContext("2d");
    let middleCanvas = this.middleCanvas;
    let middleContext = middleCanvas.getContext("2d");
    let outerCanvas = this.outerCanvas;
    let outerContext = outerCanvas.getContext("2d");

    // draw origin point
    this.drawPlanet(innerCanvas, innerContext, 9, 0);

    // draw inner orbit
    this.drawOrbit(innerCanvas, innerContext, 40);

    // draw inner planet
    this.drawPlanet(innerCanvas, innerContext, 9, 40);

    // draw middle orbit
    this.drawOrbit(middleCanvas, middleContext, 60);

    // draw middle planet
    this.drawPlanet(middleCanvas, middleContext, 5, 60);

    // draw outer orbit
    this.drawOrbit(outerCanvas, outerContext, 80);

    // draw outer planet
    this.drawPlanet(outerCanvas, outerContext, 7, 80);
  }

  drawOrbit(canvas, context, radius) {
    context.beginPath();
    context.lineWidth = 1;
    context.strokeStyle = "#0f88eb";
    context.arc(
      canvas.width / 2,
      canvas.height / 2,
      radius,
      0,
      Math.PI * 2,
      true
    );
    context.stroke();
    context.closePath();
  }

  drawPlanet(canvas, context, radius, offset) {
    context.beginPath();
    context.fillStyle = "#0f88eb";
    context.arc(
      canvas.width / 2 + offset,
      canvas.height / 2,
      radius,
      0,
      Math.PI * 2,
      true
    );
    context.fill();
    context.beginPath();
  }

  render() {
    return (
      <div>
        <p className="empty-title title-font-size">OOF~ There is nothing here :(</p>

        <div className="canvas-container">
          <canvas className="inner-canvas" width="300" height="300" ref={(canvas) => this.innerCanvas = canvas}></canvas>
          <canvas className="middle-canvas" width="300" height="300" ref={(canvas) => this.middleCanvas = canvas}></canvas>
          <canvas className="outer-canvas" width="300" height="300" ref={(canvas) => this.outerCanvas = canvas}></canvas>
        </div>
      </div>
    )
  }
}
