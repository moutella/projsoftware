package servico.controle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import anotacao.Transactional;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import servico.impl.ProdutoAppServiceImpl;
import dao.impl.ProdutoDAOImpl;

public class InterceptadorDeServico implements MethodInterceptor {
    /*
     * Parametros:
     * 
     * objeto - "this", o objeto "enhanced", isto �, o proxy.
     * 
     * metodo - o m�todo interceptado, isto �, um m�todo da interface ProdutoDAO,
     * LanceDAO, etc.
     * 
     * args - um array de args; tipos primitivos s�o empacotados. Cont�m os
     * argumentos que o m�todo interceptado recebeu.
     * 
     * metodoProxy - utilizado para executar um m�todo super. Veja o coment�rio
     * abaixo.
     * 
     * MethodProxy - Classes geradas pela classe Enhancer passam este objeto para o
     * objeto MethodInterceptor registrado quando um m�todo interceptado �
     * executado. Ele pode ser utilizado para invocar o m�todo original, ou chamar o
     * mesmo m�todo sobre um objeto diferente do mesmo tipo.
     * 
     */

    public Object intercept(Object objeto, Method metodo, Object[] args, MethodProxy metodoDoProxy) throws Throwable {
	try {
		((ProdutoDAOImpl)((ProdutoAppServiceImpl) objeto).produtoDAO).em = JPAUtil.getEntityManager();
	    if (metodo.isAnnotationPresent(Transactional.class)) {
		JPAUtil.beginTransaction();
	    }
	    
	    Object obj = metodoDoProxy.invokeSuper(objeto, args);
	    
	    if (metodo.isAnnotationPresent(Transactional.class)) {
		JPAUtil.commitTransaction();
	    }
	    
	    return obj;
	}
	catch (RuntimeException e) {
	    if (metodo.isAnnotationPresent(Transactional.class)) {
		JPAUtil.rollbackTransaction();
	    }
	    throw e;
	}
	catch (Exception e) {
	    if (metodo.isAnnotationPresent(Transactional.class)) {
		Class<?>[] classes = metodo.getAnnotation(Transactional.class).rollbackFor();
		boolean achou = false;
		for (Class<?> classe : classes) {
		    if (classe.isInstance(e)) {
			JPAUtil.rollbackTransaction();
			achou = true;
			break;
		    }
		}
		if(!achou) {
		    JPAUtil.commitTransaction();
		}
	    }

	    throw e;
	}
	finally {
	    JPAUtil.closeEntityManager();
	}
    }
}




