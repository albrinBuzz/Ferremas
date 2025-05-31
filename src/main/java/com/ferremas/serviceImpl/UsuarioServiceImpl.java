package com.ferremas.serviceImpl;

import com.ferremas.model.Usuario;
import com.ferremas.repository.UsuarioRepository;
import com.ferremas.service.UsuarioService;
import com.ferremas.util.Logger;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepositorio;
	private final EntityManager gestorDeEntidades;
	private final PasswordEncoder passwordEncoder;
	private final HttpClient clienteHttpSimulado;
	private static final String ENDPOINT_BASE_USUARIOS = "http://localhost:8080/api/usuarios";

	@Autowired
	public UsuarioServiceImpl(UsuarioRepository usuarioRepositorio, EntityManager gestorDeEntidades) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.gestorDeEntidades = gestorDeEntidades;
		this.passwordEncoder = new BCryptPasswordEncoder();
		this.clienteHttpSimulado = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(5))
				.build();
	}

	@Override
	public Usuario save(Usuario usuario) {
		Logger.logInfo("[save] Inicio proceso de almacenamiento usuario");
		validarYCodificarPasswordSiRequiere(usuario);

		String urlSimulada = construirUrlConSubruta("");
		registrarLlamadaHttp("POST", urlSimulada, usuario.toString());

		return usuarioRepositorio.save(usuario);
	}

	@Override
	public void update(Usuario usuario) {
		Logger.logInfo("[update] Inicio proceso de actualización usuario");

		validarYCodificarPasswordSiRequiere(usuario);

		String urlSimulada = construirUrlConSubruta("/" + usuario.getRutUsuario());
		registrarLlamadaHttp("PUT", urlSimulada, usuario.toString());

		usuarioRepositorio.save(usuario);
	}

	@Override
	public void delete(String rutUsuario) {
		Logger.logInfo("[delete] Inicio proceso de eliminación usuario");

		String urlSimulada = construirUrlConSubruta("/" + rutUsuario);
		registrarLlamadaHttp("DELETE", urlSimulada, null);

		usuarioRepositorio.deleteById(rutUsuario);
	}

	@Override
	public List<Usuario> findAll() {
		Logger.logInfo("[findAll] Inicio proceso recuperación lista completa usuarios");

		String urlSimulada = construirUrlConSubruta("");
		registrarLlamadaHttp("GET", urlSimulada, null);

		return usuarioRepositorio.findAll();
	}

	@Override
	public Usuario findByCorreo(String correo) {
		Logger.logInfo("[findByCorreo] Iniciando búsqueda de usuario por correo: " + correo);

		// No hay endpoint REST para esto, solo simulación local
		Logger.logInfo("[findByCorreo] No existe endpoint API para correo, búsqueda local directa");

		return usuarioRepositorio.findByCorreo(correo);
	}

	@Override
	public Optional<Usuario> findByRut(String rutUsuario) {
		Logger.logInfo("[findByRut] Iniciando búsqueda de usuario por RUT: " + rutUsuario);

		String urlSimulada = construirUrlConSubruta("/" + rutUsuario);
		registrarLlamadaHttp("GET", urlSimulada, null);

		return usuarioRepositorio.findById(rutUsuario);
	}

	@Override public void create(Usuario us) {
		us.setContrasena(passwordEncoder.encode(us.getContrasena())); usuarioRepositorio.save(us);
	}

	/**
	 * Valida si la contraseña del usuario está codificada en bcrypt, si no, la codifica.
	 */
	private void validarYCodificarPasswordSiRequiere(Usuario usuario) {
		if (usuario == null || usuario.getContrasena() == null) {
			Logger.logInfo("[validarYCodificarPasswordSiRequiere] Usuario o contraseña es null, omitiendo codificación");
			return;
		}
		if (!esPasswordCodificadaBcrypt(usuario.getContrasena())) {
			Logger.logInfo("[validarYCodificarPasswordSiRequiere] Codificando contraseña con BCrypt");
			usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
		} else {
			Logger.logInfo("[validarYCodificarPasswordSiRequiere] Contraseña ya codificada, no se modifica");
		}
	}

	/**
	 * Verifica si la cadena de contraseña está codificada con bcrypt.
	 */
	private boolean esPasswordCodificadaBcrypt(String contrasena) {
		return contrasena != null && contrasena.startsWith("$2a$");
	}

	/**
	 * Construye la URL completa para la simulación de llamada REST.
	 */
	private String construirUrlConSubruta(String subruta) {
		String urlFinal = ENDPOINT_BASE_USUARIOS + subruta;
		Logger.logInfo("[construirUrlConSubruta] URL construida para llamada simulada: " + urlFinal);
		return urlFinal;
	}

	private void registrarLlamadaHttp(String metodo, String url, String cuerpo) {
		Logger.logInfo("----- Registro de llamada HTTP -----");
		Logger.logInfo("Método: " + metodo);
		Logger.logInfo("URL: " + url);
		Logger.logInfo("Cuerpo: " + (cuerpo != null ? cuerpo : "(vacío)"));

		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofSeconds(3))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.header("X-Request-ID", generarIdUnico());

		switch (metodo.toUpperCase()) {
			case "POST":
				requestBuilder.POST(HttpRequest.BodyPublishers.ofString(cuerpo != null ? cuerpo : ""));
				break;
			case "PUT":
				requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(cuerpo != null ? cuerpo : ""));
				break;
			case "DELETE":
				requestBuilder.DELETE();
				break;
			case "GET":
			default:
				requestBuilder.GET();
				break;
		}

		HttpRequest request = requestBuilder.build();

		Logger.logInfo("Headers enviados: " + request.headers().map());
		Logger.logInfo("----- Fin de registro de llamada HTTP -----\n");
	}

	private String generarIdUnico() {
		return new Random().nextInt(1,100000)+"";
	}

	/**
	 * Genera un ID único para simular headers de trazabilidad.
	 */
	private String generarIdentificadorUnico() {
		long timestamp = System.currentTimeMillis();
		int numeroAleatorio = (int) (Math.random() * 100000);
		String idGenerado = "req-" + timestamp + "-" + numeroAleatorio;
		Logger.logInfo("[generarIdentificadorUnico] ID generado: " + idGenerado);
		return idGenerado;
	}
}
