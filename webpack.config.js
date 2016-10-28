const webpack = require('webpack');
const path = require('path');

const BUILD_DIR = path.resolve(__dirname, 'src/main/webapp/js');
const APP_DIR = path.resolve(__dirname, 'src/main/web/js');

const config = {
	entry: {
		index: APP_DIR + '/index.jsx',
		signin: APP_DIR + '/signin.jsx',
		console: APP_DIR + '/console.jsx'
	},
	output: {
		path: BUILD_DIR,
	    publicPath: '/assets/',
		filename: '[name].build.js'
	},
	resolve: {
		extensions: ['', '.js', '.jsx']
	},
	module: {
	    loaders: [{
	      test: /\.jsx?/,
	      loaders: ['babel'],
	      exclude: /(node_modules|bower_components)/
	    }, {
	      test: /\.css$/,
	      loaders: ['style', 'css']
	    }, {
	      test: /\.s[a|c]ss$/,
	      loaders: ['style', 'css', 'postcss', 'sass']
	    }, {
	      test: /\.less$/,
	      loaders: ['style', 'css', 'less']
	    }, {
	      test: /\.woff$/,
	      loader: "url-loader?limit=10000&mimetype=application/font-woff&name=[path][name].[ext]"
	    }, {
	      test: /\.woff2$/,
	      loader: "url-loader?limit=10000&mimetype=application/font-woff2&name=[path][name].[ext]"
	    }, {
	      test: /\.(eot|ttf|svg|gif|png)$/,
	      loader: "file-loader"
	    }]
	},
	devtool: 'source-map'
};

module.exports = config;


